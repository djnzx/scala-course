package shapelss.poly

object ContextIdeas extends App {

  trait Formatter {
    def format(s: String): String
  }

  trait F1 extends Formatter {
    override def format(s: String): String = s.toLowerCase
  }
  implicit object F1 extends F1

  trait F2 extends Formatter {
    override def format(s: String): String = s.toUpperCase
  }
  implicit object F2 extends F2

  case class Text1[F <: Formatter](text: String) {
    def format(implicit f: F) = f.format(text)
  }

  Text1[F1]("AbCdEf").format // abcdef
  Text1[F2]("AbCdEf").format // ABCDEF

  case class Text2(text: String) {
    def format[F <: Formatter](implicit f: F) = f.format(text)
  }

  Text2("AbCdEf").format[F1] // abcdef
  Text2("AbCdEf").format[F2] // ABCDEF
}
