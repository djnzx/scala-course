package essential

import java.time.LocalDate

sealed trait ParentVisitor {
  def id: Int
}
final case class ParentBranch(id: Int, name: String) extends ParentVisitor
class ParentInherit extends ParentVisitor {
  override def id: Int = 39
}
/**
  * When we mark a trait as sealed we must define all of its subtypes in the same file.
  * Once the trait is sealed, the compiler knows the complete set of subtypes
  * and will warn us if a pa􏰁ern matching expression is missing a case:
  */
sealed trait Visitor extends ParentInherit {
  def created: LocalDate
}
final case class Anonymous(override val id: Int, created: LocalDate) extends Visitor
final case class User(override val id: Int, email: String, created: LocalDate) extends Visitor

/**
  * Sealed traits and final (case) classes allow us to control extensibility of types.
  * The majority of cases should use the sealed trait / final case class pa􏰁ern.
  *
  * the compiler will warn if we miss a case in pattern matching
  *
  */
object SealedTrait extends App {
  val v1: Visitor = Anonymous(42, LocalDate.now)
  val v2: Visitor = User(43, "a@b.c", LocalDate.now)
}
