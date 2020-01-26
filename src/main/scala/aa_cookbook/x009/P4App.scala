package aa_cookbook.x009

object P4App extends App {
  def add1(x: Int, y: Int) = { x + y }

  val add = (x: Int, y: Int) => x + y
  val mul = (x: Int, y: Int) => x * y

  def executeAndPrint(f:(Int, Int) => Int, x: Int, y: Int): Unit = {
    println(f(x,y))
  }

  executeAndPrint(add, 2 ,3)
  executeAndPrint(add1, 2 ,3)
  executeAndPrint(mul, 2 ,3)

}
