package x94slick

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

object SlickApp04c_map extends App {
  // to select only one column from a whole query
  val name_extractor = (country: CountryTable) => country.name
  // to reverse a string
  val reverter_str = (s: String) => s.reverse
  // to apply a reverter to an optional
  val reverter_opt = (opt: Option[String]) => opt.map(reverter_str)

  val action3 = countries
    .map(_.name).result.headOption
    // mapping action
    .map(_.map(_.reverse))

  val action32 = countries
    .map((ct: CountryTable) => ct.name).result.headOption
    .map((os: Option[String]) => os.map((s: String) => s.reverse))

  // explanation
  val action1: DBIO[Option[String]] = countries.map(name_extractor).result.headOption
  val action2: DBIO[Option[String]] = action1.map(reverter_opt)

  val r: Option[String] = Await.result(db.run(action2), 2 seconds)
  println(r)

}
