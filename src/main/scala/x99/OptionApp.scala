package x99

object OptionApp extends App {

  def convert(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case e: Exception => None
    }
  }

  def message1(opt: Option[Int]): String = opt match {
    case Some(value) => s"value: $value"
    case None        => "conversion error"
  }

  def message2(opt: Option[Int]): String = opt.map(v => s"value: $v").getOrElse("conversion error")

  def message3(opt: Option[Int]): String = opt.fold("conversion error")(v => s"value: $v")

  println(message1(convert("123")))
  println(message2(convert("123a")))
  println(message3(convert("123b")))


}
