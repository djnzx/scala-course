package aa_cookbook.x009

object P2App extends App {
  val double1 = (el: Int) => { el * 2 }
  println(double1(1))

  val range = List.range(1,5)
  var range2 = range.map(double1)

  val f1: (Int) => Boolean = i => { i % 2 == 0 }
  val f2: Int => Boolean = i => { i % 2 == 0 }
  val f3: Int => Boolean = i => i % 2 == 0
  val f4: Int => Boolean = _ % 2 == 0
  val f5 = (i: Int) => i % 2 == 0

  val g1 = (x: Int, y:Int) => { x + y}
  val g2 = (x: Int, y:Int) => x + y
  val g3: (Int, Int) => Int = (x, y) => { x + y }
  val g4: (Int, Int) => Int = (x, y) => x + y

  def m1(i: Int) = i % 2 == 0
  def m2(i: Int) = { i % 2 == 0 }
  def m3(i: Int): Boolean = i % 2 == 0
  def m4(i: Int): Boolean = { i % 2 == 0 }

  def modMethod(i: Int) = i % 2 == 0
  val modFunction = (i: Int) => i % 2 == 0

  val rangeM1 = range.filter(el => modMethod(el))
  val rangeM2 = range.filter(modMethod(_))
  val rangeM3 = range.filter(modMethod)

  val rangeM4 = range.filter(el => modFunction(el))
  val rangeM5 = range.filter(modFunction(_))
  val rangeM6 = range.filter(modFunction)

  val cos1 = scala.math.cos(_)
  val cos2 = scala.math.cos _

  val pow1 = scala.math.pow(_, _)
  val pow2 = scala.math.pow _
  val powSq = scala.math.pow(_, 2)

  println(powSq(3))
  println(pow2(2, 3))



}
