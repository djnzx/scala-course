package x0lessons

import scala.language.higherKinds
import scala.util.Try

package object fp_to_the_max {

  def parseInt(s: String): Option[Int] = Try(s.toInt).toOption

  trait Program[F[_]] {
    def finish[A](a: => A): F[A]
    def chain[A, B](fa: F[A], afb: A => F[B]): F[B]
    def map[A, B](fa: F[A], ab: A => B): F[B]
  }
  object Program {
    def apply[F[_]](implicit fp: Program[F]): Program[F] = fp
  }

  // ability to use map / flatMap
  implicit class ProgramSyntax[F[_], A](fa: F[A]) {
    def map[B](f: A => B)(implicit fp: Program[F]): F[B] = fp.map(fa, f)
    def flatMap[B](afb: A => F[B])(implicit fp: Program[F]): F[B] = fp.chain(fa, afb)
  }

  def finish[F[_], A](a: => A)(implicit fp: Program[F]): F[A] = fp.finish(a)

  case class IO[A](core: () => A) { self =>
    def map[B](f: A => B): IO[B] = IO(() => f(self.core()))
    def flatMap[B](f: A => IO[B]): IO[B] = IO(() => f(self.core()).core())
  }

  trait Console[F[_]] {
    def putStrLn(line: String): F[Unit]
    def getStrLn(): F[String]
  }
  object Console {
    def apply[F[_]](implicit F: Console[F]): Console[F] = F
  }

  trait Random[F[_]] {
    def nextInt(upper: Int): F[Int]
  }
  object Random {
    def apply[F[_]](implicit F: Random[F]): Random[F] = F
  }
  def nextInt[F[_]](upper: Int)(implicit F: Random[F]): F[Int] = Random[F].nextInt(upper)


  def putStrLn[F[_]: Console](line: String): F[Unit] = Console[F].putStrLn(line)
  def getStrLn[F[_]: Console](): F[String] = Console[F].getStrLn()

  object IO {
    def point[A](a: => A): IO[A] = IO( () => a)

    implicit val ProgramIO: Program[IO] = new Program[IO] {
      override def finish[A](a: => A): IO[A] = IO.point(a)
      override def chain[A, B](fa: IO[A], afb: A => IO[B]): IO[B] = fa.flatMap(afb)
      override def map[A, B](fa: IO[A], ab: A => B): IO[B] = fa.map(ab)
    }

    import scala.io.StdIn.readLine

    implicit val ConsoleIO: Console[IO] = new Console[IO] {
      override def putStrLn(line: String): IO[Unit] = IO( () => println(line) )
      override def getStrLn(): IO[String] = IO( () => readLine() )
    }

    implicit val RandomIO: Random[IO] = new Random[IO] {
      override def nextInt(upper:  Int): IO[Int] = IO(() => scala.util.Random.nextInt(upper))
    }
  }

}
