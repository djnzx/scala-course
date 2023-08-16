package inmemoryclassloader

// exploring difference
//
// Option.flatten:
// def flatten[B](implicit ev: A <:< Option[B]): Option[B]
//
// Iterable.flatten
// def flatten[B](implicit asIterable: A => IterableOnce[B]): CC[B]
//
class ImplIdea {

  trait TC {
    val t: Int
  }

  def whatever1[A](a: A)(implicit f: A => TC) = a.t
  def whatever2[A <: TC](a: A) = a.t

  class Test extends TC {
    override val t: Int = 13
  }
  val test = new Test

  whatever1(test)
  whatever2(test)

}
