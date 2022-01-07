import cats._

import cats.implicits._
import cats.effect.MonadCancel

import scala.concurrent.duration.FiniteDuration

package object utils {

  implicit class DebugWrapper[F[_], A](fa: F[A]) {
    def debug(implicit functor: Functor[F]): F[A] = fa.map { a =>
      val t = Thread.currentThread().getName
      println(s"[$t] $a")
      a
    }
  }

  def unsafeSleep[F[_], E](duration: FiniteDuration)(implicit mc: MonadCancel[F, E]): F[Unit] =
    mc.pure(Thread.sleep(duration.toMillis))

}
