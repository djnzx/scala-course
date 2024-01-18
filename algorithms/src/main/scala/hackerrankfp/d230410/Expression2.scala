package hackerrankfp.d230410

object Expression2 {
  val opsAll = LazyList('*', '+', '-')
  implicit class CharOps(c: Char) {
    def calc(a: BigInt, b: Long): BigInt = c match {
      case '+' => a + b
      case '-' => a - b
      case '*' => a * b
    }
  }
  case class A(xa: BigInt, ops: List[Char]) {
    def collect(op: Char, x: Int): A = A(op.calc(xa, x), op :: ops)
  }

  def doSolve(a: A, ns: List[Int]): LazyList[A] = ns match {
    case Nil if a.xa % 101 == 0 => LazyList(a)
    case Nil     => LazyList.empty
    case n :: ns => opsAll.flatMap(op => doSolve(a.collect(op, n), ns))
  }

  def solve(numbers: List[Int]) = {
    val ops = doSolve(A(numbers.head, Nil), numbers.tail).head.ops.reverse
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
