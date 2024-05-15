package common

import org.scalacheck.{Arbitrary, Gen}

trait ArbitraryFromGen {

  implicit def genArbitrary[A](implicit gen: Gen[A]): Arbitrary[A] = Arbitrary(gen)

}
