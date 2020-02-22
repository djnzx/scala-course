package aa_fp

case class User(
                 id: Int,
                 name: Name,
                 billingInfo: BillingInfo,
                 phone: String,
                 email: String
               )

case class Name(
                 firstName: String,
                 lastName: String
               )

case class Address(
                    street1: String,
                    street2: String,
                    city: String,
                    state: String,
                    zip: String
                  )

case class CreditCard(
                       name: Name,
                       number: String,
                       month: Int,
                       year: Int,
                       cvv: String
                     )

case class BillingInfo(
                        creditCards: Seq[CreditCard]
                      )
