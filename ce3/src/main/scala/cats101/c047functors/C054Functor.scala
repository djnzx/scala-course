package cats101.c047functors

object C054Functor extends App {
  trait Functor[F[_]] {
    def map[A, B](f: A => B): F[B]
  }

  //  // Declare F using underscores:
  //  def myMethod[F[_]] = {
  //    // Reference F without underscores:
  //    val functor = Functor.apply[F]
  //    // ...
  //  }

  // there is the strong analogy with functions

  // Declare f specifying parameters:
  val f = (x: Int) => x * 2
  // Reference f without parameters:
  val f2 = f andThen f

  /**
    * functor LAWS:
    *
    * 1. fa.map(a => a) == fa
    * 2. fa.map(g(f(_))) == fa.map(f).map(g)
    */

}
