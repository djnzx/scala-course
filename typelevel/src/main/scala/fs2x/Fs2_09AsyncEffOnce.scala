package fs2x

import fs2._
import cats.MonadError
import cats.effect.IO

object Fs2_09AsyncEffOnce extends App {

  trait Connection {
    def readBytes(onSuccess: Array[Byte] => Unit, onFailure: Throwable => Unit): Unit

    // or perhaps
    def readBytesE(onComplete: Either[Throwable,Array[Byte]] => Unit): Unit =
      readBytes(bs => onComplete(Right(bs)), e => onComplete(Left(e)))

    override def toString = "<connection>"
  }

  trait Async[F[_]] extends MonadError[F, Throwable] {
    /**
    Create an `F[A]` from an asynchronous computation, which takes the form
   of a function with which we can register a callback. This can be used
   to translate from a callback-based API to a straightforward monadic
   version.
      */
    def async[A](register: (Either[Throwable,A] => Unit) => Unit): F[A]
  }

  val c: Connection = new Connection {
    def readBytes(onSuccess: Array[Byte] => Unit, onFailure: Throwable => Unit): Unit = {
      Thread.sleep(200)
      onSuccess(Array(0,1,2))
    }
  }
  // c: AnyRef with Connection = <connection>

  val bytes: IO[Array[Byte]] =
    cats.effect.Async[IO]
      .async[Array[Byte]] { (cb: Either[Throwable,Array[Byte]] => Unit) =>
        c.readBytesE(cb)
      }
  // bytes: IO[Array[Byte]] = IO$351791287

  val r: Vector[List[Byte]] =
    Stream.eval(bytes)
      .map(_.toList)
      .compile
      .toVector
      .unsafeRunSync()
  // res46: Vector[List[Byte]] = Vector(List(0, 1, 2))
  println(r)

}
