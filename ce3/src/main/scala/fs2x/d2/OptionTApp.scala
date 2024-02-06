package fss.d2

import cats.data.OptionT
import cats.effect.IO
import cats.implicits._

object OptionTApp extends App {

  object Lifting {
    val a1: OptionT[IO, Int] = OptionT.pure[IO](2)
    val a2: IO[Option[Int]] = a1.value

    val b1: OptionT[IO, String] = OptionT.some[IO]("qwe")
    val b2: IO[Option[String]] = b1.value

    val c1: OptionT[IO, Double] = OptionT.none[IO, Double]
    val c2: IO[Option[Double]] = c1.value

    val d1: OptionT[IO, Boolean] = OptionT.fromOption[IO](Some(true))
    val d2: IO[Option[Boolean]] = d1.value

    val e1: OptionT[IO, Int] = OptionT.liftF(IO(13))
    val e3: IO[Option[Int]] = e1.value

    val e2: OptionT[List, Int] = OptionT.liftF(List(13, 14, 15))
    val e4: List[Option[Int]] = e2.value

    val e5: OptionT[IO, Char] = OptionT(IO(Option('x')))
    val e6: IO[Option[Char]] = e5.value

    val cond = "abc".contains("a")
    val f1: OptionT[IO, String] = OptionT.when[IO, String](cond)("OK")
    val f2: IO[Option[String]] = f1.value

    val f3: OptionT[IO, Int] = OptionT.whenF(cond)(IO(11))
    val f4: IO[Option[Int]] = f3.value

    val g1: OptionT[IO, String] = OptionT.unless[IO, String](cond)("OK")
    val g2: IO[Option[String]] = g1.value

    val g3: OptionT[IO, Int] = OptionT.unlessF(cond)(IO(11))
    val g4: IO[Option[Int]] = g3.value
  }

  object Transformation {
    import Lifting.a1

    val a: IO[Int] = a1.fold(-3)(_ + 1)
    val b: IO[String] = a1.foldF(IO("33"))(x => IO(x.toString))
    a1.foreachF(x => IO(println(x)))
    val c = a1.map(_ + 1)

  }

}
