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

  def numbers_select_all: Unit = {
    val action = partnumbers.result
    exec(action) foreach println
  }

  // monadic style, using foreign key, comprehension
  def numbers_select_joined1: Unit = {
    val join1 = for {
      pn <- partnumbers
      ve <- pn.vendor
      if (ve.name like "%OP%") || (pn.number like "%Z%")
      if ve.name inSet Seq("A", "B", "C")
    } yield (pn.id, ve.name, pn.number)

    exec(join1.result) foreach println
  }

  // monadic style, using foreign key, without comprehension
  def numbers_select_joined2: Unit = {
    val join2 = partnumbers flatMap { pn =>
      pn.vendor.map { ve =>
        (pn.id, ve.name, pn.number)
      }
    }
    exec(join2.result) foreach println
  }

  // join without foreign key
  def numbers_select_joined3: Unit = {
    val join3 = for {
      pn <- partnumbers
      ve <- vendors
      if pn.vendor_id === ve.id
    } yield (pn.id, ve.name, pn.number)
    exec(join3.result) foreach println
  }

  def numbers_select_joined41: Unit = {
    val join41: Query[(PartNumbers, VendorTable), (PartNumber, Vendor), Seq] = partnumbers
//      .join(vendors) on (_.vendor_id === _.id)
      .join(vendors) on ((pn: PartNumbers, ve: VendorTable) => pn.vendor_id === ve.id)
    exec(join41.result) foreach println
  }

  def numbers_select_joined42: Unit = {
    val join41: Query[(PartNumbers, VendorTable), (PartNumber, Vendor), Seq] = partnumbers
      .join(vendors) on { case (pn, ve) => pn.vendor_id === ve.id }

    val join42 = join41.map { case (pn: PartNumbers, ve: VendorTable) => (pn.id, ve.name, pn.number)}
    exec(join42.result) foreach println
  }

  /**
    * leftJoin
    * rightJoin
    * crossJoin
    * produces Option[field] instead of possible nulls
    *
    * https://stackoverflow.com/questions/26815913/how-to-do-or-filter-in-slick
    */
}
