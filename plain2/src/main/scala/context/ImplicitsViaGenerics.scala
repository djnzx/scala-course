package context

object ImplicitsViaGenerics extends App {

  def toUpper(s: String) = s.toUpperCase
  def toLower(s: String) = s.toLowerCase

  trait Formatter[X] {
    def format(s: String): String
  }

  trait UpperCase
  object UpperCase {

    /** it can be packed into object */
    implicit val instance: Formatter[UpperCase] = toUpper
  }

  trait LowerCase {

    /** it can be packed into trait */
    implicit val instance: Formatter[LowerCase] = toLower
  }

  /** and pulled to object */
  object LowerCase extends LowerCase

  case class Data(x: String) {
    def print[A](implicit f: Formatter[A]) = println(f.format(x))
  }

  Data("qweASD").print[UpperCase] // QWEASD
  Data("qweASD").print[LowerCase] // qweasd

}
