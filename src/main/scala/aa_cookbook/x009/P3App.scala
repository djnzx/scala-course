package aa_cookbook.x009

object P3App extends App {
  def execute(callback: () => Unit) = {
    println("before")
    val cb = callback // !!! just an expression
    println(callback)
    callback()
    println("after")
  }

  execute( () => println("A"))

  def plusOne1(i: Int) = i + 1
  println(plusOne1(1))

  def plusOne2(i: Int): Unit = i + 1
  println(plusOne2(1))

  val toInt = (s: String) => s.toInt;

  def execute2(f:(String) => Int) = {
    f("6")
  }
  def execute3(f: String => Int) = {
    f("8")
  }
  def execute4(f: (String, Int, Double) => Seq[String]) = {
    f("66", 66, 66.6)
  }
  def execute5(f: Int => Unit): Unit = { println(77) }
  def execute6(f: Int => Unit): Int = { 1 }
  def execute7(f: Int => Unit) = {}

  execute5(_ => {})
  println(execute6(_ => {}))
}
