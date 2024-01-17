package hackerrankfp.d230410_14

object Solution {
  val opsAll       = List('+', '-', '*')
  implicit class CharOps(c: Char) {
    def calc = (a: Long, b: Long) =>
      c match {
        case '+' => a + b
        case '-' => a - b
        case '*' => a * b
      }
  }
  case class A(xa: Long, ops: List[Char], found: Boolean = false) {
    def collect(op: Char, x: Int): A = A(op.calc(xa, x), op :: ops)
  }
  object A {
    def init(xa: Long) = A(xa, Nil)
    def zero           = init(0)
  }
  val mod          = 101
  private val data = scala.collection.mutable.Map.empty[(Long, List[Int]), A]

  def doSolve(a: A, nums: List[Int]): A = {

    lazy val logic = nums match {
      case Nil     => a.copy(found = a.xa % mod == 0)
      case n :: ns =>
        opsAll.foldLeft(A.zero) { (a1, op) =>
          if (a1.found) a1
          else doSolve(a.collect(op, n), ns)
        }
    }

    data.getOrElseUpdate((a.xa % mod, nums), logic)
  }

  def solve(numbers: List[Int]) = {
    val ops = doSolve(A.init(numbers.head), numbers.tail).ops.reverse
    represent(numbers, ops)
  }

  def represent(numbers: List[Int], ops: List[Char]) =
    numbers.tail.zip(ops).foldLeft(new StringBuilder(numbers.head.toString)) { case (a, (n, op)) => a ++= s"$op$n" }.toString

  def main(args: Array[String]): Unit = {
    import scala.io.StdIn
    val _  = StdIn.readLine()
    val ns = StdIn.readLine().split(" ").map(_.toInt).toList
    println(solve(ns))
  }
}
