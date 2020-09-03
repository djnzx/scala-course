package catseffx.copy_app

import java.io.{File, FileInputStream, FileOutputStream, InputStream, OutputStream}

import cats.effect.{IO, Resource}
import cats.implicits.{catsSyntaxFlatMapOps, catsSyntaxTuple2Semigroupal}

object Copy1 {
  /**
    * creating resource
    */
  def inputStream(f: File): Resource[IO, FileInputStream] =
    Resource.make {
      IO(new FileInputStream(f))                         // how to build
    } { is: FileInputStream =>
      IO(is.close())                                     // how to release
        .handleErrorWith((_: Throwable) => IO.unit)      // how to handle errors
    }
  /**
    * creating resource
    */
  def outputStream(f: File): Resource[IO, FileOutputStream] =
    Resource.make {
      IO(new FileOutputStream(f))                         // build 
    } { os: FileOutputStream =>
      IO(os.close())
        .handleErrorWith((_: Throwable) => IO.unit)      // SHOULD BE LOGGED
    }

  /**
    * we could do that way, but it doesn't give ability to handle errors
    *   def inputStream(f: File): Resource[IO, FileInputStream] =
    *     Resource.fromAutoCloseable(IO(new FileInputStream(f)))
    */
  /**
    * combining resources and generalizing 
    */
  def inputOutputStreams(in: File, out: File): Resource[IO, (InputStream, OutputStream)] =
    for {
      is <- inputStream(in)
      os <- outputStream(out)
    } yield (is, os)

  /**
    * Resource is based on Bracket, 3 stages:
    * - acquisition
    * - usage
    * - release
    */
  def transmit(is: InputStream, os: OutputStream, buf: Array[Byte], acc: Long): IO[Long] =
    for {
      amount <- IO(is.read(buf, 0, buf.length))
      count  <- if(amount > -1) 
                   IO(os.write(buf, 0, amount)) >>
                   transmit(is, os, buf, acc + amount) // recursive call
                else IO.pure(acc) // End of read stream reached (by java.io.InputStream contract), nothing to write
    } yield count                 // Returns the actual amount of bytes transmitted

  def transfer(origin: InputStream, destination: OutputStream): IO[Long] = for {
    buffer <- IO(new Array[Byte](1024 * 10))
    number <- transmit(origin, destination, buffer, 0L) 
  } yield number
  
  
  def copy(origin: File, destination: File): IO[Long] = 
    inputOutputStreams(origin, destination)
//      .use { (transfer _).tupled }
      .use { case (in, out) => transfer(in, out) }
      
  def copy_bracket(origin: File, destination: File): IO[Long] = {
    val inIO: IO[InputStream]  = IO(new FileInputStream(origin))
    val outIO:IO[OutputStream] = IO(new FileOutputStream(destination))

    (inIO, outIO)              // Stage 1: Getting resources 
      .tupled                  // From (IO[InputStream], IO[OutputStream]) to IO[(InputStream, OutputStream)]
      .bracket {
        case (in, out) => transfer(in, out)    // Stage 2: Using resources (for copying data, in this case)
      } {
        case (in, out) =>      // Stage 3: Freeing resources
          (IO(in.close()), IO(out.close()))
            .tupled              // From (IO[Unit], IO[Unit]) to IO[(Unit, Unit)]
            .handleErrorWith(_ => IO.unit).void
      }
  }

  /**
    * cats experiments
    */
  val x1: IO[(Int, String)] = (IO(123), IO("Hello")).tupled
  val x2: IO[(Int, String)] = for {
    i <- IO(123)
    s <- IO("String")
  } yield (i, s)
  val z: (IO[Int], IO[String]) = (x1.map(_._1), x1.map(_._2))
}
