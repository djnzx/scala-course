package catseffx

import java.io._

import cats.Applicative
import cats.effect.concurrent.Semaphore
import cats.effect.{Concurrent, Console, ExitCode, IO, IOApp, Resource, Sync}
import cats.implicits._

object CopyApp extends IOApp {
  type FIS = FileInputStream
  type FOS = FileOutputStream
  type IS = InputStream
  type OS = OutputStream
  type IEX = IllegalArgumentException

  /**
    * File => Resource
    */
  def mkRsInput[F[_]](f: File, sem: Semaphore[F])(implicit F: Sync[F]): Resource[F, FIS] =
  // first parameter   - how to acquire
  // second parameter - how to release
    Resource.make {
      F.delay(new FIS(f))
    } { is: FIS =>
      sem.withPermit {
        F.delay(is.close())
          .handleErrorWith { _: Throwable => F.pure(()) }
      }
    }

  /**
    * File => Resource
    */
  def mkRsOutput[F[_]](f: File, sem: Semaphore[F])(implicit F: Sync[F]): Resource[F, FOS] =
    Resource.make {
      F.delay(new FOS(f))
    } { os: FOS =>
      sem.withPermit {
        F.delay(os.close())
          .handleErrorWith { _: Throwable => F.pure(()) }
      }
    }

  /**
    * combination
    */
  def mkResources[F[_]: Sync](fin: File, fout: File, sem: Semaphore[F]): Resource[F, (FIS, FOS)] =
    for {
      is <- mkRsInput(fin, sem)
      os <- mkRsOutput(fout, sem)
    } yield (is, os)

  /**
    * transfer WHOLE content
    */
  def transmit[F[_]: Console](is: IS, os: OS, buffer: Array[Byte], acc: Long)(implicit F: Sync[F]): F[Long] =
    for {
      amount <- F.delay(is.read(buffer, 0, buffer.length))
      _      <- Console[F].putStr(".") // F[Unit]. actually we do the same stuff, but under the hood
      _      <- F.delay(print("#"))        // F[Unit]
      count  <- if (amount == -1) F.pure(acc)
                else // write + flatMap (recursively) next step (IO is Stack Safe)
                  F.delay(os.write(buffer, 0, amount)) >>
                    transmit(is, os, buffer, acc + amount)
    } yield count

  /**
    * transfer ONE buffer
    * it isn't cancellable since it hasn't wrapped into .use
    */
  def transfer[F[_]: Console](is: FIS, os: FOS)(implicit F: Sync[F]): F[Long] =
    for {
      buffer <- F.delay(new Array[Byte](128)) // buffer = 128 bytes
      total  <- transmit(is, os, buffer, 0L)
    } yield total

  /**
    * if there is a problem (ex) during outIO, inIO won't be closed
    * with multiple resources - use Resource!
    */
  def copyBracketEditionWoSemaphore(origin: File, destination: File): IO[Long] = {
    val inIO : IO[FileInputStream]  = IO(new FileInputStream(origin))
    val outIO: IO[FileOutputStream] = IO(new FileOutputStream(destination))

    import cats.effect.Console.implicits._       // Console[IO]

    (inIO, outIO)                                // ( IO[FIS], IO[FOS] )
      .tupled                                    // IO[(FIS, FOS)]
      .bracket {                                 // IO.bracket
        case (in, out) => transfer[IO](in, out)  // real job
      } {                                        // Freeing resources
        case (in, out) =>
          (IO(in.close()), IO(out.close()))      // ( IO[FIS => Unit], IO[FOS => Unit] )
            .tupled                              // IO[ (FIS => Unit, FOS => Unit) ]
            .handleErrorWith { _: Throwable => IO.unit }
            .void
      }
  }

  /**
    * `main` function
    * it IS cancellable since it HAS wrapped into .use
    *
    * Console could be passed through implicit of `copy` caller or as a parameter
    */
  def copy[F[_]: Console](origin: File, destination: File)(implicit F: Concurrent[F]): F[Long] =
    for {
      sem   <- Semaphore[F](1)
      res: Resource[F, (FIS, FOS)] = mkResources(origin, destination, sem)
      result: F[Long] = res.use { case (fis, fos) =>
                 // body which should be cancellable is wrapped into .withPermit
                 val r: F[Long] = sem.withPermit(transfer(fis, fos))
                 r
               }
      count <- result
  } yield count

  def printErr(msg: String): Unit = scala.Console.err.println(msg)

  def validate[F[_]: Applicative](args: List[String]): F[Either[String, (File, File)]] =
    (args match {
      case n1::n2::_ if n1 != n2 => (new File(n1), new File(n2)).asRight
      case n1::_::_              => s"Source and destination files mustn't be similar!: $n1".asLeft
      case _                     => "Need TWO file names as a params".asLeft
    }).pure[F]

  override def run(args: List[String]): IO[ExitCode] = {
    import cats.effect.Console.implicits._       // Console[IO]

    for {
      vr  <- validate[IO](args)
      v0 = vr match {
        case Left(errMsg)  => IO(printErr(errMsg))
        case Right((f1,f2)) =>
      }

      _   <- IO(printErr("Hello"))
      fn1 = args(0)
      fn2 = args(1)
      _   <- if (args.length < 2) IO.raiseError(new IEX("Need TWO file names as a params"))
             else IO.unit
      _   <- if (fn1 == fn2) IO.raiseError(new IEX(s"source and destination files mustn't be similar!: $fn1"))
             else IO.unit
      src = new File(fn1)
      dst = new File(fn2)
      cnt <- copy[IO](src, dst)
      _   <- IO(println(s"\n$cnt bytes copied from ${src.getPath} to ${dst.getPath}"))

    } yield ExitCode.Success
  }
}
