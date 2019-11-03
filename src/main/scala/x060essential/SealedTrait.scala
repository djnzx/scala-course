package x060essential

import java.time.LocalDate

/**
  * When we mark a trait as sealed we must define all of its subtypes in the same file.
  * Once the trait is sealed, the compiler knows the complete set of subtypes
  * and will warn us if a pa􏰁ern matching expression is missing a case:
  */
sealed trait Visitor {
  def id: Int;
  def created: LocalDate;
}
final case class Anonymous(id: Int, created: LocalDate) extends Visitor
final case class User(id: Int, email: String, created: LocalDate) extends Visitor

/**
  * Sealed traits and final (case) classes allow us to control extensibility of types.
  * The majority of cases should use the sealed trait / final case class pa􏰁ern.
  *
  * the compiler will warn if we miss a case in pattern matching
  *
  */
object SealedTrait extends App {
  val v1 = Anonymous(42, LocalDate.now)
  val v2 = User(43, "a@b.c", LocalDate.now)

}
