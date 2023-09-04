package scalacheck.extrasyntax

import org.scalacheck.Gen

import scala.util.chaining.scalaUtilChainingOps

trait GenExtraSyntax {

  private object impl {
    def const[A](ga: Gen[A]): Gen[A] = Gen.const(ga.sample.get)
  }

  implicit class GenOps2[A](ga: Gen[A]) {

    /** make Any generator constant TO ONE VALUE */
    def const: Gen[A] = impl.const(ga)

    /** make CONSTANT LIST from Any generator */
    def constListOfN(n: Int): Gen[List[A]] = impl.const(Gen.listOfN(n, ga))

    /** take random element from {{{Gen[Seq[A]]}}} and produce {{{Gen[A]}}} */
    def oneOf[B](implicit ev: A <:< Seq[B]): Gen[B] = ga.map(ev).flatMap(x => Gen.oneOf(x))

    def notPresentIn(as: Seq[A]): Gen[A] = ga.retryUntil(a => !as.contains(a))

    def notPresentIn(gas: Gen[Seq[A]]): Gen[A] = notPresentIn(gas.sample.get)

    def listOfUpToN(n: Int): Gen[List[A]] =
      (0 to n).map(x => 1 -> Gen.listOfN(x, ga))
        .pipe(x => Gen.frequency(x: _*))

  }

}
