package x99


object EitherApp extends App {

  def convert(s: String): Either[String, Int] = {
    try {
      Right(s.toInt)
    } catch {
      case e: Exception => Left("conversion error")
    }
  }

  def message1(either: Either[String, Int]): String = either match {
    case Right(value) => s"value: $value"
    case Left(msg)    => msg
  }

  def message3(either: Either[String, Int]): String =
    either.fold(s => s, int => s"value: $int")

  println(message1(convert("123")))
  println(message1(convert("123a")))
  println(message3(convert("123")))
  println(message3(convert("123b")))

}
