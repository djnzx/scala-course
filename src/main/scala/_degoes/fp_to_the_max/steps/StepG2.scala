package _degoes.fp_to_the_max.steps

object StepG2 extends App {

  trait Console[F[_]] {
    def pline(line: String): F[Unit]
    def rline()            : F[String]
  }

  final case class IO[A](run: () => A) {
    def iomap   [B](f: A => B)    : IO[B] = IO.of(f(run()))
    def ioflatMap[B](f: A => IO[B]): IO[B] = f(run())
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
    def pline(line: String): (TestData, Unit  ) = (copy(output = output :+ line), ())
    def rline():             (TestData, String) = (copy(input = input.tail), input.head)
    def check(expected: List[String]): Option[Boolean] = Some(output == expected)
    def results: String = output.mkString("\n")
  }

  final case class IOTest[A](run: TestData => (TestData, A)) { me =>

    def iomap   [B](f: A => B)        : IOTest[B] = IOTest(t => {
      val tda: (TestData, A) = me.run(t)
      tda match {
        case (td, a) => (td, f(a))
      }
    })

    def ioflatMap[B](f: A => IOTest[B]): IOTest[B] = IOTest(t => {
      val (td: TestData, a: A) = me.run(t)  // running logic. td - modified data, a - result
      val iob: IOTest[B] = f(a)             // applying `f` to value returned
      val tdb: (TestData, B) = iob.run(td)
      tdb
//      me.run(t) match { case (td, a) => f(a).run(td) }
    })

    def eval(initial: TestData): TestData = run(initial)._1
  }

  final object IOTest {
    implicit val consoleIO2: Console[IOTest] = new Console[IOTest] {
      override def pline(line: String): IOTest[Unit]   = new IOTest( t => t.pline(line) )
      override def rline()            : IOTest[String] = new IOTest( t => t.rline() )
    }

    implicit val programIO2: Program[IOTest] = new Program[IOTest] {
      override def pmap   [A, B](fa: IOTest[A], f: A => B)        : IOTest[B] = fa.iomap(f)
      override def pflatMap[A, B](fa: IOTest[A], f: A => IOTest[B]): IOTest[B] = fa.ioflatMap(f)
    }
  }

  // wiring `pline` & `rline` to appropriate wrapper IO | IOTest by `implicitly[Console[F]]`
  def pline[F[_]](line: String)(implicit f: Console[F]): F[Unit]   = f.pline(line)
  def rline[F[_]]()            (implicit f: Console[F]): F[String] = f.rline()

  val message1 = "Enter number A"
  val message2 = "Enter number B"
  val rs1 = "A="
  val rs2 = "B="
  val rs3 = "Sum="

  val add = (a: Int, b: Int) => a + b
  implicit val stoi = (s: String) => s.toInt

  def app[F[_]: Program: Console]: F[Unit] = for {
    _    <- pline(message1)
    n1   <- rline()
    _    <- pline(s"$rs1$n1")
    _    <- pline(message2)
    n2   <- rline()
    _    <- pline(s"$rs2$n2")
    sum = add(n1, n2)
    _    <- pline(s"$rs3$sum")
  } yield ()

  println("-")
//  app[IO].run()
  val u1 = "5"
  val u2 = "1"
  val testData = TestData(List(u1, u2), Nil)
  val expected = List(message1, s"$rs1$u1", message2, s"$rs2$u2", s"$rs3${add(u1, u2)}")
//  app[IOTest] eval testData check expected foreach println
  app[IOTest].eval(testData).output foreach println
}
