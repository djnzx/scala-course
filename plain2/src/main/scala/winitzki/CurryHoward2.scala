package winitzki

import io.chymyst.ch.{implement, ofType}

import scala.reflect.runtime.universe._

/** https://github.com/Chymyst/curryhoward */
class CurryHoward2 extends Base {

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
