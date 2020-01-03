package _degoes.tips12

abstract class Tip10DescribeDontDo {

  // brake down complexity
  trait TP { def charge(o: Int): Unit }
  val processor: TP
  def sendEmail(value: Any) = ???
  def genConfirm(i: Int) = ???
  object Charge {
    def apply(i: Int): Any = ???
  }
  object SendEmail {
    def apply(value: Nothing): Any = ???
  }
  def completePurchaseBAD(order: Int): Unit = {
    // standard approach
    processor.charge(order)
    val email = genConfirm(order)
    sendEmail(email)
  }
  // testable !!!
  def completePurchaseGOOD(order: Int): List[Any] = {
    // better approach
    val email = genConfirm(order)
    Charge(order) :: SendEmail(email) :: Nil
  }
}
