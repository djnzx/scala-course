package winitzki

import scala.reflect.runtime.universe._

// Ex5.4.2.11
class CurryHoward3 extends Base {

  def f1[Z, A, B](z: Z, a1: A, a2: A, f: A => B): (Z, B, B) =
    (z, f(a1), f(a2))

  case class State[S, A](s: S, a: A)

  def getType[A: TypeTag]: Type = weakTypeOf[A]

  def f2[S, A: TypeTag, B: TypeTag](sa: State[S, A], f: (S, A) => B): State[S, B] = {
    val r: Boolean = getType[A] =:= getType[B]
    ???
  }

}
