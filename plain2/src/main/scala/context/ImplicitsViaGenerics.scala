package context

object ImplicitsViaGenerics extends App {

  def toUpper(s: String) = s.toUpperCase
  def toLower(s: String) = s.toLowerCase

  trait Formatter[X] {
    def format(s: String): String
  }

  trait UpperCase {
    implicit val instance: Formatter[UpperCase] = toUpper
  }
  object UpperCase extends UpperCase

  trait LowerCase {
    implicit val instance: Formatter[LowerCase] = toLower
  }
  object LowerCase extends LowerCase

  case class Data(x: String) {
    def print[A](implicit f: Formatter[A]) = println(f.format(x))
  }

  Data("qweASD").print[UpperCase] // QWEASD
  Data("qweASD").print[LowerCase] // qweasd

}
