package fps

import com.softwaremill.quicklens._

object Fps110 extends App {
  val user = User(
    id = 1,
    name = Name(
      firstName = "Al",
      lastName = "Alexander"
    ),
    billingInfo = BillingInfo(
      creditCards = Seq(
        CreditCard(
          name = Name("John", "Doe"),
          number = "1111111111111111",
          month = 3,
          year = 2020,
          cvv = ""
        )
      )
    ),
    phone = "907-555-1212",
    email = "al@al.com"
  )

  val user1 = user.modify(_.phone).setTo("720-555-1212")
  val user2 = user1.modify(_.email).setTo("al@example.com")
  val u3 = user.modify(_.phone).setTo("720-555-1212")
    .modify(_.email).setTo("al@example.com")
    .modify(_.name.firstName).setTo("Alvin")

}
