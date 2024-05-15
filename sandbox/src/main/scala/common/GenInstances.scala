package common

import cats.Functor
import cats.Semigroupal
import org.scalacheck.Gen

trait GenInstances {

  implicit val functorGen: Functor[Gen] = new Functor[Gen] {
    override def map[A, B](fa: Gen[A])(f: A => B): Gen[B] = fa.map(f)
  }
  implicit val semiGen: Semigroupal[Gen] = new Semigroupal[Gen] {
    override def product[A, B](fa: Gen[A], fb: Gen[B]): Gen[(A, B)] =
      fa.flatMap(a => fb.map(b => a -> b))
  }

}
