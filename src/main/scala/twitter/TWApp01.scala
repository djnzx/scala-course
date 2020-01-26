package twitter

object TWApp01 extends App {

  trait Cache[K, V] {
    def get(key: K): V
    def put(key: K, value: V): Unit
    def delete(key: K): Unit
  }

//  class AddOne extends Function1[Int, Int] {
//  class AddOne extends ((Int) => Int) {
  class AddOne extends (Int => Int) {
    override def apply(v1: Int): Int = v1 + 1
  }

  val addOne = new AddOne
  val x1 = addOne.apply(1)
  val x2 = addOne(1)
  println(x1)
  println(x2)

  case class Calculator(brand: String, model: String)
  val hp20b = Calculator("HP", "20b")
  val hp30b = Calculator("HP", "30B")

  def calcType(calc: Calculator): String = calc match {
    case Calculator("HP", "20B") => "financial"
    case Calculator("HP", "48G") => "scientific"
    case Calculator("HP", "30B") => "business"
    case Calculator(b, m) => "Calculator: %s %s is of unknown type".format(b, m)
    case Calculator(_, _) => "Calculator of unknown type"
    case Calculator(_, _) => "Calculator of unknown type".format(calc)
    case cc @ Calculator(_, _) => "Calculator: %s of unknown type".format(cc)
    case _ => "Calculator of unknown type"
  }

  val one: PartialFunction[Int, String] = { case 1 => "one" }
  val two: PartialFunction[Int, String] = { case 2 => "two" }
  val three: PartialFunction[Int, String] = { case 3 => "three" }
  val wildcard: PartialFunction[Int, String] = { case _ => "something else" }
  val partial: PartialFunction[Int, String] = one orElse two orElse three
  val full: PartialFunction[Int, String] = partial orElse wildcard


}
