package degoes.fp_to_the_max.steps

import scala.util.{Success, Try}

object StepG3 extends App {

  trait Console[F[_]] {
    def pline(line: String): F[Unit]
    def rline(): F[String]
  }

  trait Program[F[_]] {
    def finish[A](a: => A): F[A]
    def map[A, B](fa: F[A], f: A => B): F[B]
    def flatMap[A, B](fa: F[A], f: A => F[B]): F[B]
  }
  implicit class ProgramSyntax[F[_]: Program, A](fa: F[A]) {
    def map[B](f: A => B)(implicit pf: Program[F]): F[B] = pf.map(fa, f)
    def flatMap[B](f: A => F[B])(implicit pf: Program[F]): F[B] = pf.flatMap(fa, f)
  }

  case class IO[A](run: () => A) { me =>
    def map[B](f: A => B): IO[B] = IO( () => f(me.run()) )
    def flatMap[B](f: A => IO[B]): IO[B] = f(me.run())
  }

  object IO {
//    def of[A](a: => A): IO[A] = IO(() => a)

    implicit val programIO: Program[IO] = new Program[IO] {
      override def map[A, B](fa: IO[A], f: A => B): IO[B] = fa.map(f)
      override def flatMap[A, B](fa: IO[A], f: A => IO[B]): IO[B] = fa.flatMap(f)
      override def finish[A](a: => A): IO[A] = IO( () => a )
    }

    implicit val consoleIO: Console[IO] = new Console[IO] {
      override def pline(line: String): IO[Unit] = IO(() => scala.Console.out.println(line))
      override def rline(): IO[String] = IO(() => scala.io.StdIn.readLine())
    }
  }

  case class MockIO[A](run: TestData => (TestData, A)) { me =>
    def map[B](f: A => B): MockIO[B] = MockIO(t =>
      me.run(t) match {
        case (td, a) => (td, f(a))
      })
    def flatMap[B](f: A => MockIO[B]): MockIO[B] = MockIO(t =>
      me.run(t) match {
        case (td, a) => f(a).run(td)
      })
    def eval(testData: TestData): TestData = run(testData)._1
  }
  object MockIO {
    implicit val programMockIO: Program[MockIO] = new Program[MockIO] {
      override def map[A, B](fa: MockIO[A], f: A => B): MockIO[B] = fa.map(f)
      override def flatMap[A, B](fa: MockIO[A], f: A => MockIO[B]): MockIO[B] = fa.flatMap(f)
      override def finish[A](a: => A): MockIO[A] = MockIO( t => (t, a) )
    }

    implicit val consoleMockIO: Console[MockIO] = new Console[MockIO] {
      override def pline(line: String): MockIO[Unit] = MockIO( t => t.pline(line))
      override def rline(): MockIO[String] = MockIO( t => t.rline())
    }
  }

  def pline[F[_]](str: String)(implicit c: Console[F]) = c.pline(str)
  def rline[F[_]]             (implicit c: Console[F]) = c.rline()
  def finishWith[F[_], A](a: => A)(implicit pa: Program[F]) = pa.finish(a)

  def app[F[_] : Program : Console] = for {
    _     <- pline("Enter number A:")
    n1    <- rline
    _     <- pline("Enter number B:")
    n2    <- rline
    sum = n1.toInt + n2.toInt
    _     <- pline(s"Sum=$sum")
  } yield ()

  case class TestData(input: List[String], output: List[String]) {
    def pline(line: String): (TestData, Unit) = (copy(output = output :+ line), ())
    def rline(): (TestData, String) = (copy(input = input.tail), input.head)
  }

//  app[IO].run()

//  val testData = TestData(List("1", "2"), Nil)
//  app[MockIO].eval(testData).output foreach println

  def app_loop_rec[F[_]: Program: Console](str: String): F[Unit] = for {
    _     <- pline("Enter number:")
    s     <- rline
    _     <- if (s == "quit")
              for {
                _ <- pline("Quitting...")
                _ <- finishWith(())
              } yield () else
              for {
                _ <- Try(s.toInt) match {
                       case Success(i) => pline(s"Number: $i")
                       case _          => pline(s"Not a umber: $s")
                     }
                _ <- app_loop_rec(str)
              } yield ()
  } yield ()

  def app2[F[_]: Program: Console] = for {
    _     <- pline("Enter name:")
    name  <- rline
    _     <- pline(s"Hello, $name")
    _     <- app_loop_rec(name)
    _     <- pline(s"Bye, $name")
  } yield ()

//  app2[IO].run()

  def factorial[F[_]: Program : Console](number: Long, acc: Long = 1): F[Long] = for {
    _ <- pline(s"${number.toString}: ${acc.toString}")
    n <- if (number <= 1) finishWith(acc) else factorial(number - 1, acc * number)
  } yield n

  def app3[F[_] : Program : Console] = for {
    _     <- pline("Enter number")
    n     <- rline.map(_.toLong)
    f     <- factorial(n)
    _     <- pline(s"$n! = $f")
  } yield ()


  println(":ev:")
  app3[IO].run() // max working number: 1295

}
