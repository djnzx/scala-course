package cats101.validated

import cats.data.NonEmptyList
import cats.implicits._

object ValidatedApp extends App {

  implicit class NelSyntax[A](x: A) {
    def nel: NonEmptyList[A] = NonEmptyList.one(x)
  }

  def validate(x: Int) = x.validNel[String]
    .ensure("should > 1".nel)(_ > 1)
    .ensure("should > 10".nel)(_ > 10)
    .ensure("should > 100".nel)(_ > 100)
    .ensure("should > 1000".nel)(_ > 1000)

  val v1 = validate(1)  // Invalid(NonEmptyList(should > 1))
  val v2 = validate(11) // Invalid(NonEmptyList(should > 100))
  val v  = v1 |+| v2    // Invalid(NonEmptyList(should > 1, should > 100))

  println(v1)
  println(v2)
  println(v)
}
