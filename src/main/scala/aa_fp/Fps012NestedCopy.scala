package aa_fp

object Fps012NestedCopy extends App {

  case class Name(first: String, middle: String, last: String)
  case class CreditCard(name: Name, number: String, month: Int, year: Int, cvv: String)
  case class BillingInfo(cards: Seq[CreditCard])
  case class User(id: Long, name: Name, billingInfo: BillingInfo, phone: String, email: String)
  // we need to sort them in topological order
  // Name -> CreditCard -> BillingInfo -> User

  // 1 - Name
  val name = Name("Jim", "M", "Rich")
  // 2 - Credit Cards
  val cc1 = CreditCard(name, "1111-2222-3333-4444", 11, 20, "123")
  val cc2 = CreditCard(name, "5555-6666-7777-8888", 12, 24, "456")
  val cc = Seq(cc1, cc2)
  // 3 - BillingInfo
  val billing = BillingInfo(cc)
  // 4 - User
  val jim_user = User(1, name, billing, "111-222-33-44", "jim@abc.com")
  /**
    * if something is changed - we need update everything to the top object
    * in this case - User
    */
}
