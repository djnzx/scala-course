package x00topics.implicits.i4tinstance_by_type.Application06

/**
  * a type class means a trait with at least one type variable
  * actually it defines a set of types: CanChat[String], CanChat[Int], ...
  */
trait CanChat[A] {
  def chat_(a: A): String
}
