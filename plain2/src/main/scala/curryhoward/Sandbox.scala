package curryhoward

import io.chymyst.ch.implement
import io.chymyst.ch.ofType
import scala.reflect.runtime.universe._

/** https://github.com/Chymyst/curryhoward */
object Sandbox extends App {

  def f[X, Y]: X => Y => X = implement
  val r: Int = f(123)("test")
  def map2[E, A, B, C](readerA: E => A, readerB: E => B, f: A => B => C): E ⇒ C = implement

  class Test[E, A, B, C] {
    val fn = map2[E, A, B, C] _
  }

  println(reify(new Test[String, Int, Double, Boolean].fn))

  {
    case class User(name: String, id: Long)

    val x: Int = 123
    val s: String = "abc"
    val f: Int ⇒ Long = _.toLong
    val b: Boolean = true

    val user = ofType[User](f, s, x, b)
    println(user)
  }
}
object Ex54211 {
  def f1[Z, A, B](z: Z, a1: A, a2: A, f: A => B): (Z, B, B) =
    (z, f(a1), f(a2))

  case class State[S, A](s: S, a: A)

  def getType[A: TypeTag]: Type = weakTypeOf[A]

  def f2[S, A: TypeTag, B: TypeTag](sa: State[S, A], f: (S, A) => B): State[S, B] = {
    val r: Boolean = getType[A] =:= getType[B]
    ???
  }

}
