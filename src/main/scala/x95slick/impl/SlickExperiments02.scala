package x95slick.impl

import x95slick.db.DatabaseLayer

final class SlickExperiments02 {
  private val persistence = new DatabaseLayer

  // import variables to local scope
  import persistence._
  import persistence.profile.api._ // actually we import slick.jdbc.PostgresProfile.api._

  def numbers_insert: Unit = {
    val action = partnumbers ++= Seq(
      PartNumber(1, "12345678QW"),
      PartNumber(2, "2345678QWE"),
      PartNumber(3, "345678QWRT"),
      PartNumber(4, "45678QWERT")
    )
    exec(action)
  }

  def numbers_select: Unit = {
    val action = partnumbers.result
    exec(action) foreach println
  }

  def numbers_select_joined: Unit = {
    val join1 = for {
      pn <- partnumbers
      ve <- pn.fk_vendor
      if ve.name like "%OP%"
      if pn.number like "%Z%"
    } yield (pn.id, ve.name, pn.number)

    val join2 = partnumbers
      .join(vendors)
//      .filter((pn: PartNumbers, ve: VendorTable) => ve.name like "%OP%")
//      .filter((pn: PartNumbers, ve: VendorTable) => pn.id > 5L)
//      .map((pn: PartNumbers, ve: VendorTable) => (pn.id, ve.name, pn.number))

    exec(join1.result) foreach println
  }

}
