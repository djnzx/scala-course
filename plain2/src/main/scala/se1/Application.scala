package se1

import java.util.Date
import java.util.UUID

object Application {

  val j = 10

  val s1: Any = if (j % 2 == 0) "Even"
  val s2: String = if (j % 2 == 0) "Even" else "Odd"

// bad
  //                  100000000
  //  case class Customer(id: Int, name: String, surName: String)
  //                  200000000
  //  case class Account(id: Int)
  sealed trait Currency
  object USD extends Currency
  object EUR extends Currency

  sealed trait AccType

  // good
  // 1.
  case class Customer(id: UUID, name: String, surName: String, accounts: Seq[Account], turnover: Int, number: Int)
  case class Account(id: UUID, typ: AccType, iban: String, currency: Currency, crDate: Date)
  case class Transaction(id: UUID, date: Date, from: Account, to: Account, currency: Currency, amount: BigDecimal)
  // 2.
  def createCustomer1(name: String, surName: String): Customer = ???
  def createAccount(currency: Currency, typ: AccType): Account = ???

  def attachAccountToCustomer1(customer: Customer, account: Account): Customer = ???
  def attachAccountToCustomer2(customer: Customer, account: Account): Unit = ???
  // 3.
  def load[A](id: UUID): A = ???
  val c: Customer = load[Customer](UUID.randomUUID())
  val a: Account = load[Account](UUID.randomUUID())
  // 4. API
  /**   - endpoint
    *   - request type
    *   - response type
    */
  trait Request
  trait Response
  case class Ok() extends Response
  case class Err() extends Response

  trait ParseError
  trait NotFound

  type ServiceHandler = Request => Option[Response]
  def validatedHandle(h: ServiceHandler): ServiceHandler = ???

  // actual server call
  def serve(handler: ServiceHandler): Unit = ???

  def loggedHandler(handler: ServiceHandler): ServiceHandler =
    (rq: Request) => {
      println(rq)
      val r: Option[Response] = handler(rq)
      r
    }

  def timedHandler(handler: ServiceHandler): ServiceHandler =
    (rq: Request) => {
      val time = System.currentTimeMillis()
      val r: Option[Response] = handler(rq)
      val spent = System.currentTimeMillis() - time
      println(spent)
      r
    }

  val businessLogic: ServiceHandler = ???
  val timed: ServiceHandler = timedHandler(businessLogic)
  val logged: ServiceHandler = loggedHandler(timed)

  serve(businessLogic)
  serve(timed)
  serve(logged)

  // in term of server, will cover all 3 functions
  def handle1(rq: Request): Option[Response] = ???
  def handle2(rq: Request): Either[NotFound, Response] = ???

  def extractPayload1[A](rq: Request): Option[A] = ???
  def extractPayload2[A](rq: Request): Either[ParseError, A] = ???

  case class CreateCustomerRq(name: String, surName: String)

  trait API {

    def createCustomer(rq: Request): Response = {
      val payload: Either[ParseError, CreateCustomerRq] = extractPayload2[CreateCustomerRq](rq)

//      payload match {
//        case Right(ccr) => Ok(createCustomer1(ccr.name, ccr.surName))
//        case Left(x) => Err(x)
//      }
      ???
    }
  }

  def isEligibleToLoanSeq(customer: Customer, criteria: Seq[Customer => Boolean]): Boolean =
    criteria.forall(cr => cr(customer))

  def more100number(customer: Customer) = customer.number > 100
  def more1000turnover(customer: Customer) = customer.turnover > 1000
  def customerStartsA(customer: Customer) = customer.name.startsWith("A")

  def isEligibleToLoan(customer: Customer) =
    isEligibleToLoanSeq(customer, Seq(more100number, more1000turnover))

  def isEligibleTo10000(customer: Customer) =
    isEligibleToLoanSeq(customer, Seq(more100number, more1000turnover, customerStartsA))

}
