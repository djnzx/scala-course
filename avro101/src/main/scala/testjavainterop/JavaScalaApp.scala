package testjavainterop

object JavaScalaApp extends App {

  def doSomething(f: BiFunc[Int, Int, Int])(a: Int, b: Int) =
    f.make(a, b)

  /** lambda will be lifted automatically to Java Interface */
  val x: Int = doSomething((a, b) => a + b)(1, 2)
  println(x)

  /** method reference will be lifted automatically to Java Interface */
  def f1(a: Int, b: Int) = a + b
  val y: Int = doSomething(f1)(1, 2)
  println(y)

  val f2 = (a: Int, b: Int) => a + b

  /** Scala f: (Int, Int) => Int can't be lifted to Java interface */
  //  val y: Int = doSomething(f2)(1, 2)
  /** needs to be rewritten */
  val jf2: BiFunc[Int, Int, Int] = (a: Int, b: Int) => f2(a, b)
  val z: Int = doSomething(jf2)(1, 2)
  println(z)

}
