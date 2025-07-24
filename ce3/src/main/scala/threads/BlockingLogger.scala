package threads

import cats.effect.Sync
import cats.implicits._
import org.typelevel.log4cats.Logger

object BlockingLogger {

  def make[F[_]: Sync](l: Logger[F]): Logger[F] = {

    implicit class BlockingOps[A](fa: F[A]) {
      def blocking: F[A] = Sync[F].blocking(fa).flatten
    }

    new Logger[F] {
      def error(t: Throwable)(msg: => String): F[Unit] = l.error(t)(msg).blocking
      def warn(t: Throwable)(msg: => String): F[Unit] = l.warn(t)(msg).blocking
      def info(t: Throwable)(msg: => String): F[Unit] = l.info(t)(msg).blocking
      def debug(t: Throwable)(msg: => String): F[Unit] = l.debug(t)(msg).blocking
      def trace(t: Throwable)(msg: => String): F[Unit] = l.trace(t)(msg).blocking
      def error(msg: => String): F[Unit] = l.error(msg).blocking
      def warn(msg: => String): F[Unit] = l.warn(msg).blocking
      def info(msg: => String): F[Unit] = l.info(msg).blocking
      def debug(msg: => String): F[Unit] = l.debug(msg).blocking
      def trace(msg: => String): F[Unit] = l.trace(msg).blocking
    }
  }

}
