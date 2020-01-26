package slick.x94slick

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object SlickApp04f_sequence extends App {
  /**
    * function which is taking (c: Country)
    * and produces ONE UPDATE STATEMENT: DBIO[Int]
    * update "country" set "name" = ? where "country"."id" = ?
    * countries - basic sql: `select * from country`
    * filter() - where clause: `select "name", "names", "id" from "country" where "id" = 1`
    * map() - fields to select added: `select "name" from "country" where "id" = 1`
    * update() - updated sql generated based on map and filter: `update "country" set "name" = ? where "country"."id" = 1"`
    */
  val reverse_two: Country => DBIO[Int] = c => countries
    .filter(_.id === c.id)
    .map((ct: CountryTable) => (ct.name, ct.names))
    .update((c.name.reverse, c.names.reverse))

  def reverse_one(c: Country): DBIO[Int] = countries
    .filter(_.id === c.id)
    .map(_.name)
    .update(c.name.reverse)
  /**
    * select all the records by running`select * from country`
    * produces: Seq[Country]
    */
  val action_select: DBIO[Seq[Country]] = countries.filter(_.id < 10L).result
  /**
    * taking countries: Seq[Country] from previous query
    * running bunch of actions update
    * just by calling reverse_one(country1)
    * DBIO.sequence() produces SINGLE action from set of actions
    */
  val action_update = (countries: Seq[Country]) => DBIO.sequence(countries.map(reverse_two))

  val action_combined: DBIO[Seq[Int]] = action_select flatMap action_update

  Await.result(db.run(action_combined), 5 seconds)
}
