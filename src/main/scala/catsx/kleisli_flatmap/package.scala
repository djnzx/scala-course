package catsx

package object kleisli_flatmap {

  // all functions `A => F[B]` or A => F[A] (we treat B=A)
  val f1:  Int => List[Int]    = (x: Int) => List(x + 1, x - 1)
  val f2:  Int => List[Int]    = (x: Int) => List(x, -x)
  val f2a: Int => List[String] = (x: Int) => List(s"$x", s"${-x}")
  val f3:  Int => List[Int]    = (x: Int) => List(x * 2, x / 2)

}
