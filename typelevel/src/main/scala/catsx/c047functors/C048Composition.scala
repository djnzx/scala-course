package catsx.c047functors

object C048Composition extends App {
  // all af them are functions Int => Int
  val f = (x: Int) => x + 1
  val g = (x: Int) => x * 2

  val fg1 = f compose g
  val fg2 = (x: Int) => f(g(x))

  val gf1 = f andThen g
  val gf2 = (x: Int) => g(f(x))

  println(fg2(100)) // (100 * 2) + 1 = 201
  println(fg1(100)) // (100 * 2) + 1 = 201
  println(gf2(100)) // (100 + 1) * 2 = 202
  println(gf1(100)) // (100 + 1) * 2 = 202
}
