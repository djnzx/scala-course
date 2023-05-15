package hackerrankfp.d230410_14

// https://www.hackerrank.com/challenges/expressions/problem
object Expression extends App {

  sealed abstract class Op {
    override def toString: String  = this match {
      case Op.Add => "+"
      case Op.Sub => "-"
      case Op.Mul => "*"
    }
    def apply(a: Int, x: Int): Int = this match {
      case Op.Add => a + x
      case Op.Sub => a - x
      case Op.Mul => a * x
    }
  }
  object Op {
    case object Add extends Op
    case object Sub extends Op
    case object Mul extends Op
    val all: List[Op] = List(Add, Sub, Mul)
  }

  def valid(x: Int) = x % 101 == 0

  def show(ns: Array[Int], ops: List[Op]) =
    ops.indices
      .foldLeft(new StringBuilder(ns.head.toString) -> ops) {
        case ((sb, op :: ops), idx) => sb.append(op).append(ns(idx + 1)) -> ops
        case _                      => throw new RuntimeException("`ops` designed to be one number less than `ns`")
      }
      ._1
      .result

  def variations(n: Int): List[List[Op]] = {
    def go(n: Int, outcome: List[List[Op]]): List[List[Op]] = n match {
      case 0 => outcome
      case n => go(n - 1, outcome.flatMap(ops => Op.all.map(_ :: ops)))
    }
    go(n, List(Nil))
  }

  def find(numbers: Array[Int]): String =
    variations(numbers.length - 1)
      .map { ops =>
        val r = (ops zip numbers.tail).foldLeft(numbers.head) { case (a, (op, x)) => op(a, x) }
        r -> ops
      }
      .find { case (r, _) => valid(r) }
      .map { case (r, ops) => show(numbers, ops) + s" = $r" }
      .getOrElse(throw new RuntimeException("supposed to exist"))

  println(
    find(
      Array(55, 3, 45, 33, 25)
    )
  )

  // TODO: optimize to use on big datasets

//  val x =
    variations(3)
//  println(x.length)
    .foreach(println)

}
