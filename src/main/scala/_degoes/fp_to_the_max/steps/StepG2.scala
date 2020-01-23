package _degoes.fp_to_the_max.steps

object StepG2 extends App {

  trait Console[F[_]] {
    def pline(line: String): F[Unit]
    def rline()            : F[String]
  }

  final case class IO[A](run: () => A) {
    def iomap   [B](f: A => B)    : IO[B] = IO.of(f(run()))
    def ioflatMap[B](f: A => IO[B]): IO[B] = IO.of(f(run()).run())
  }

  trait Program[F[_]] { me =>
    def pmap   [A, B](fa: F[A], f: A => B)   : F[B]
    def pflatMap[A, B](fa: F[A], f: A => F[B]): F[B]
  }

  implicit class ProgramSyntax[F[_]: Program, A](fa: F[A]) {
    def map[B]   (f: A => B)   (implicit pf: Program[F]): F[B] = pf.pmap(fa, f)
    def flatMap[B](f: A => F[B])(implicit pf: Program[F]): F[B] = pf.pflatMap(fa, f)
  }

  final object IO {
    def of[A](a: => A): IO[A] = new IO(() => a)

    implicit val consoleIO: Console[IO] = new Console[IO] {
      override def pline(line: String): IO[Unit]   = IO.of(scala.Console.out.println(line))
      override def rline()            : IO[String] = IO.of(scala.io.StdIn.readLine())
    }

    implicit val programIO: Program[IO] = new Program[IO] {
      override def pmap   [A, B](fa: IO[A], f: A => B)    : IO[B] = fa.iomap(f)
      override def pflatMap[A, B](fa: IO[A], f: A => IO[B]): IO[B] = fa.ioflatMap(f)
    }
  }

  final case class TestData(input: List[String], output: List[String]) {
    // read line from the mocked console + return new TestData w/o this line
    def rline()            : (TestData, String) = (copy(input = input.drop(1)), input.head)
    // write line to mocked console + return new TestData w/printed line
    def pline(line: String): (TestData, Unit) = (copy(output = output :+ line), ())
    // obtain final result
    def results: String = output.mkString("\n")
  }

  final case class IOTest[A](run: TestData => (TestData, A)) { me =>

    def iomap   [B](f: A => B)        : IOTest[B] = new IOTest(td => {
      val tda: (TestData, A) = me.run(td)
      tda match {
        case (td, a) => (td, f(a))
      }
    })

    def ioflatMap[B](f: A => IOTest[B]): IOTest[B] = new IOTest(td => {
      val tda: (TestData, A) = me.run(td)    // running logic
      val td1: TestData = tda._1             // after logic: TestData modified
      val a: A = tda._2                      // after logic: value returned
      val tdb: (TestData, B) = f(a).run(td1) // applying `f` to value returned
      tdb
    })

    def eval(initial: TestData): TestData = run(initial)._1
  }

  final object IOTest {
    implicit val consoleIO2: Console[IOTest] = new Console[IOTest] {
      override def pline(line: String): IOTest[Unit]   = new IOTest( t => t.pline(line) )
      override def rline()            : IOTest[String] = new IOTest( t => t.rline() )
    }

    implicit val programIO2: Program[IOTest] = new Program[IOTest] {
      override def pmap[A, B](fa: IOTest[A], f: A => B): IOTest[B] = fa.iomap(f)
      override def pflatMap[A, B](fa: IOTest[A], f: A => IOTest[B]): IOTest[B] = fa.ioflatMap(f)
    }
  }

  def pline[F[_]](line: String)(implicit f: Console[F]): F[Unit]   = f.pline(line)
  def rline[F[_]]()            (implicit f: Console[F]): F[String] = f.rline()

  def app[F[_]: Program: Console]: F[Unit] = for {
    _    <- pline("Enter number A")
    n1   <- rline()
    _    <- pline(s"A=$n1")
    _    <- pline("Enter number B")
    n2   <- rline()
    _    <- pline(s"B=$n2")
    sum: Int = n1.toInt + n2.toInt
    _    <- pline(s"Sum = $sum")
  } yield ()

//  app[IO].run()
  val testData = TestData(List("5","1"), Nil)
  app[IOTest].eval(testData).output foreach println
}
