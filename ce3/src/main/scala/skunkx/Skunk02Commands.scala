package skunkx

import cats._
import cats.effect._
import cats.implicits._
import java.time.LocalDate
import skunk._
import skunk.codec.all._
import skunk.data.Completion
import skunk.implicits._

object Skunk02Commands extends SkunkExploreApp {

  // A command is a SQL statement that does not return rows.
  val a: Command[Void] = sql"SET SEED TO 0.123".command

  val update1: Command[String *: String *: EmptyTuple] =
    sql"""
      UPDATE country
      SET    headofstate = $varchar
      WHERE  code = ${bpchar(3)}
    """.command

  case class Info(code: String, hos: String)

  val update2: Command[Info] = update1.contramap { case Info(code, hos) => code *: hos *: EmptyTuple }

  val update3: Command[Info] = update1.to[Info]

  //                                                 "DELETE FROM country WHERE name IN ($1, $2, $3)"
  def deleteMany(n: Int): Command[List[String]] = sql"DELETE FROM country WHERE name IN (${varchar.list(n)})".command
  val delete3                                   = deleteMany(3)
  pprintln(delete3)

  def insertMany(n: Int): Command[List[(String, Short)]] = {
    val enc = (varchar ~ int2).values.list(n)
    // "INSERT INTO pets VALUES ($1, $2), ($3, $4), ($5, $6)"
    sql"INSERT INTO pets VALUES $enc".command
  }
  val insert3                                            = insertMany(3)
  pprintln(insert3)

  def insertExactly(ps: List[(String, Short)]): Command[ps.type] = {
    val enc = (varchar ~ int2).values.list(ps)
    sql"INSERT INTO pets VALUES $enc".command
  }
  val pairs                                                      = List[(String, Short)](("Bob", 3), ("Alice", 6))

  // "INSERT INTO pets VALUES ($1, $2), ($3, $4)"
  val insertPairs: Command[pairs.type] = insertExactly(pairs)
  pprintln(insertPairs)

  override val app = sessionR.use { s =>
    for {
      prepared       <- s.prepare(insertPairs)
      cc: Completion <- prepared.execute(pairs)
      cs: Completion <- s.execute(a)
      _              <- pprintlnF(cs)
    } yield ()
  }
}
