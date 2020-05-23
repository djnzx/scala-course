package fps

object Fps009PAFandCurrying extends App {
  /**
    * partial application takes function with N parameters
    * and returns function with M'<'N parameters
    *
    */
  // original function (req 3 params, 3 groups)
  def plus_3g(a: Int)(b: Int)(c: Int): Int = a + b + c
  // partially applied 1 or 2
  def plus1_2a: Int => Int => Int = plus_3g(1)
  def plus1_2b: Int => Int        = plus_3g(1)(2)
  def plus1_2c: Int => Int        = plus1_2a(2)
  // we can use it
  val r1 = plus1_2a(2)(3) // 6
  val r2 = plus1_2b(3)    // 6
  val r3 = plus1_2c(3)    // 6
  // partially applied to non-consecutive params
  def plus2_1d: Int => Int = plus_3g(1)(_)(3)
  // use it
  val r4 = plus2_1d(2)

  val func3: Int => Int => Int => Int = plus_3g _
  println((plus_3g _).isInstanceOf[_ => _ => _ => _])

  def plus(a: Int, b: Int, c: Int): Int = a + b + c
  println((plus _).isInstanceOf[(_,_,_) => _])

  val plus_c: Int => Int => Int => Int = (plus _).curried
  println(plus_c.isInstanceOf[_ => _ => _ => _])
  println((plus _).curried.isInstanceOf[_ => _ => _ => _])
  println((plus _).curried.isInstanceOf[Function1[Int, Function1[Int, Function1[Int, Int]]]])

}
