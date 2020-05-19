package catseffx

import cats.effect.{IO, Resource}
import java.io.{File, FileInputStream, FileOutputStream, InputStream, OutputStream}
//import cats.instances.tuple._
//import cats.syntax.semigroup._
import cats.implicits._

object CopyApp {

  /**
    * File => Resource
    */
  def inputStream(f: File): Resource[IO, FileInputStream] =
    // first parameter   - how to acquire
    // second parameter - how to release
    Resource.make {
      IO(new FileInputStream(f))                         // acquire
    } { is: FileInputStream =>
      IO(is.close())
        .handleErrorWith { _: Throwable =>
          // that's the way how to handle exception (different ones)
          IO.unit }                                      // release
    }

  /**
    * File => Resource
    */
  def outputStream(f: File): Resource[IO, FileOutputStream] =
    Resource.make {
      IO(new FileOutputStream(f))
    } { os: FileOutputStream =>
      IO(os.close())
        .handleErrorWith { _: Throwable => IO.unit }
    }

  /**
    * combination
    */
  def streams(fin: File, fout: File): Resource[IO, (FileInputStream, FileOutputStream)] =
    for {
      is <- inputStream(fin)
      os <- outputStream(fout)
    } yield (is, os)

  def transmit(is: InputStream, os: OutputStream, buffer: Array[Byte], acc: Long): IO[Long] =
    for {
      amount <- IO(is.read(buffer, 0, buffer.length))
      count  <- if (amount == -1) IO.pure(acc)
                else // write + flatMap (recursively) next step (IO is Stack Safe)
                  IO(os.write(buffer, 0, amount)) >>
                    transmit(is, os, buffer, acc + amount)
    } yield count

  /**
    * real job
    */
  def transfer(is: FileInputStream, os: FileOutputStream): IO[Long] =
    for {
      buffer <- IO(new Array[Byte](1024*10))
      total  <- transmit(is, os, buffer, 0L)
    } yield total

  /**
    * if there is a problem (ex) during outIO, inIO won't be closed
    * with multiple resources - use Resource!
    */
  def copyBracketEdition(origin: File, destination: File): IO[Long] = {
    val inIO : IO[FileInputStream]  = IO(new FileInputStream(origin))
    val outIO: IO[FileOutputStream] = IO(new FileOutputStream(destination))

    (inIO, outIO)                                // ( IO[FIS], IO[FOS] )
      .tupled                                    // IO[(FIS, FOS)]
      .bracket {                                 // IO.bracket
        case (in, out) => transfer(in, out)      // real job
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
    */
  def copy(origin: File, destination: File): IO[Long] =
    streams(origin, destination)
      .use { case (fis, fos) => transfer(fis, fos) }

}
