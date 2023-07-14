package skunkx

import cats._
import cats.effect._
import cats.implicits._
import java.time.LocalDate
import skunk._
import skunk.codec.all._
import skunk.implicits._

object Skunk01Queries extends SkunkExploreApp {

  val fr1: Fragment[Void]        = sql"select current_date"
  val q1: Query[Void, LocalDate] = fr1.query(date)

  val fr2: Fragment[Void]     = sql"select name from country"
  val q2: Query[Void, String] = fr2.query(varchar) // text vs varchar

  val q3: Query[Void, String ~ Int] =
    sql"SELECT name, population FROM country".query(varchar ~ int4)

  case class Country(name: String, population: Int)

  val countryDecoder: Decoder[Country] =
    (varchar ~ int4).map { case (n, p) => Country(n, p) }

  val q4: Query[Void, Country] =
    sql"SELECT name, population FROM country".query(countryDecoder)

  val countryDecoder2: Decoder[Country] =
    (varchar *: int4).to[Country]

  /** s.execute 0..N
    * s.unique  1
    * s.option  0/1
    */

  val q5: Query[String, Country] =
    sql"""
    SELECT name, population
    FROM   country
    WHERE  name LIKE $varchar
  """.query(countryDecoder2)

  val q6: Query[String *: Int *: EmptyTuple, Country] =
    sql"""
      SELECT name, population
      FROM   country
      WHERE  name LIKE $varchar
      AND    population < $int4
    """.query(countryDecoder2)

  val app: IO[Unit] = sessionR.use { s: Session[IO] =>
    for {
      d1 <- s.unique(q1) // guarantee result is one line
      _  <- printlnF(s"The current date is $d1.")

      a0 <- s.execute(q2)
      a1 <- s.execute(q3)
      a2 <- s.execute(q4)
      as <- s.execute(q5)("U%")
      _  <- as.traverse(pprintlnF(_))
      _  <- dividerF
      _  <- s.prepare(q5).flatMap { pq =>
              pq.stream("U%", 64)
                .evalMap(pprintlnF)
                .compile
                .drain
            }
      _  <- dividerF
      _  <- s.prepare(q6).flatMap { ps =>
              ps.stream(("U%", 2000000), 64)
                .evalMap(pprintlnF)
                .compile
                .drain
            }

    } yield ()
  }

}
