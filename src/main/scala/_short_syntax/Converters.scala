package _short_syntax

object Converters extends App {
  val conv1: Int => Char = {
    case 1 => 'A'
    case 2 => 'B'
    case _ => 'z'
  }
  def conv2(i: Int): Char = i match {
    case 1 => 'A'
    case 2 => 'B'
    case _ => 'z'
  }

  println(conv1(1))
  println(conv2(2))
}
