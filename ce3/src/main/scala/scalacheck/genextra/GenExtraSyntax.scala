package scalacheck.genextra

import cats.Applicative
import org.scalacheck.Gen

import java.util.UUID

trait GenExtraSyntax {

  implicit val applicativeGen: Applicative[Gen] = new Applicative[Gen] {
    override def pure[A](x: A): Gen[A] = Gen.const(x)
    override def ap[A, B](ff: Gen[A => B])(fa: Gen[A]): Gen[B] = ff.flatMap(f => fa.map(f))
  }

  /** object-related syntax */
  implicit class GenCompanionOps(g: Gen.type) {

    def wrappedUuid[A](f: UUID => A): Gen[A] = Gen.uuid.map(f)

    val none: Gen[Option[Nothing]] = Gen.const(None)

    def boolean(truePercentage: Int = 50): Gen[Boolean] =
      Gen.frequency(
        truePercentage       -> true,
        100 - truePercentage -> false
      )

    def prefixedUuid(prefix: String): Gen[String] = Gen.uuid.map(uuid => prefix.concat(uuid.toString))

    def prefixedStringOfN(n: Int, prefix: String, genChar: Gen[Char] = Gen.alphaNumChar): Gen[String] =
      Gen.stringOfN(n, genChar).map(x => prefix.concat(x))

  }

  /** already existing generator syntax */
  implicit class GenOps[A](ga: Gen[A]) {

    def some: Gen[Option[A]] = Gen.some(ga)

    def option: Gen[Option[A]] = Gen.option(ga)

    /** make Any generator constant TO ONE VALUE */
    def const: Gen[A] = Gen.const(ga.sample.get)

    def listOfN(n: Int): Gen[List[A]] = Gen.listOfN(n, ga)

    def listOf(min: Int, max: Int): Gen[List[A]] = {
      val distribution = (min to max).map(x => 1 -> ga.listOfN(x))
      Gen.frequency(distribution: _*)
    }

    /** make CONSTANT LIST from Any generator */
    def constListOfN(n: Int): Gen[List[A]] = Gen.const(Gen.listOfN(n, ga).sample.get)

    /** take random element from {{{Gen[Seq[A]]}}} and produce {{{Gen[A]}}} */
    def oneOf[B](implicit ev: A <:< Seq[B]): Gen[B] = ga.map(ev).flatMap(x => Gen.oneOf(x))

    def notPresentIn(as: Seq[A]): Gen[A] = ga.retryUntil(a => !as.contains(a))

    def notPresentIn(gas: Gen[Seq[A]]): Gen[A] = notPresentIn(gas.sample.get)

    /** list of values A of length 0..N */
    def listOfUpToN(n: Int): Gen[List[A]] = {
      val distribution = (0 to n).map(x => 1 -> Gen.listOfN(x, ga))
      Gen.frequency(distribution: _*)
    }
  }

}
