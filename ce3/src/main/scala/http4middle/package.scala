import io.circe.generic.AutoDerivation

package object http4middle {

  final case class Student(age: Int, name: String, message: String)
  object Student extends AutoDerivation

}
