package udemy.scala_beginners.lectures.part3fp._either

object EitherApp extends App {

  def validateName(name: String): Either[String, String] = {
    if (name.isEmpty) Left("Name cannot be empty")
    else Right(name)
  }

  val inputName = "alex"
  //val inputName = ""
  val vn = validateName(inputName).right.map(_.toUpperCase)
  println(vn)

  val vn2 = validateName(inputName).fold (
    error => s"Validation failed: $error",
    result => s"Validation succeeded: $result"
  )
  println(vn2)

  val vn3 = validateName(inputName) match {
    case Left(error) => s"Validation failed: $error"
    case Right(result) => s"Validation succeeded: $result"
  }
  println(vn3)

  val vn4 = validateName(inputName).right.toOption
  println(vn4)

  if (vn4.isDefined) println(vn4.get)
  val vn5 = vn4.getOrElse("_error")
  println(vn5)

}
