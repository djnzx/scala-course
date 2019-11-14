package x060essential

import scala.util.Try

object X191Calc extends App {
  // MONAD! responsible for conversion errors
  def parse(num: String) = Try(num.toInt).toOption

  // MONAD! Representation responsible for division by zero
  def divide(o1: Int, o2: Int) = if (o2 == 0) None else Some(o1 / o2)

  // MONAD! responsible for wrong operation
  def do_op(o1: Int, o2: Int, op: String) = op match {
    case "+" => Some(o1 + o2)
    case "-" => Some(o1 - o2)
    case "*" => Some(o1 * o2)
    case "/" => divide(o1, o2)
    case _   => None
  }

  // MONAD! responsible for whole process
  def calc(n1: String, n2: String, op: String) = for {
    o1 <- Seq(11,12,13,14,15)//parse(n1)
    o2 <- parse(n2)
    res <- do_op(o1, o2, op)
  } yield res

  def calc_v2(n1: String, n2: String, op: String) =
    parse(n1).flatMap(n1a =>
      parse(n2).flatMap(n2a =>
        do_op(n1a, n2a, op)))

  // final output formatting
  implicit class OptionFormatted(o: Option[Int]) {
    def formatted: String = o match {
      case Some(n) => s"result is: $n"
      case None    => "wrong input"
    }
  }

  // test area
  val dataset = Seq(
    ("1","2","+"),
    ("3","2","-"),
    ("3","2","("), // 3rd list
    ("3","2","*"),
    ("6","3","/"),
    ("1","0","/"), // 6th list
  )
  dataset.foreach(t => println(calc_v2(t._1, t._2, t._3) formatted))
  dataset.foreach(t => println(calc(t._1, t._2, t._3)))
}
