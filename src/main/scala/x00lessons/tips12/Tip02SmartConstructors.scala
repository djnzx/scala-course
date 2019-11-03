package x00lessons.tips12

object Tip02SmartConstructors extends App {
  /**
    * the problem:
    * case class Email(value: String)
    * val email = Email("")
    */

  def sendEmail(email: Email) = ??? // undefined behavior
  // compromise, private constructor
  sealed class Email private (value: String)
  object Email {
    def fromString(origin: String): Option[Email] =
      if (origin.length > 6) Some(new Email(origin)) else None
  }
  val e1: Option[Email] = Email.fromString("")
  println(e1) // None
  val e2: Option[Email] = Email.fromString("ab@i.ua")
  println(e2) // Some


}
