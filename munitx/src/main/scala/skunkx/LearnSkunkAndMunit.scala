package skunkx

import cats._
import cats.effect._
import cats.implicits._
import java.time.LocalDate
import munit.CatsEffectSuite
import skunk._
import skunk.codec.all._
import skunk.data.Completion
import skunk.implicits._

/** https://github.com/typelevel/skunk
  *
  * docker run -p5432:5432 -d tpolecat/skunk-world
  *
  * https://typelevel.org/skunk/tutorial/Setup.html
  * https://typelevel.org/skunk/tutorial/Query.html
  * https://typelevel.org/skunk/tutorial/Command.html
  */
class LearnSkunkAndMunit extends CatsEffectSuite
    with SkunkConnection with Tools {

  test("1.s imple query. unique result") {

    /** fragment requires Noting */
    val fr: Fragment[Void] = sql"select current_date"

    /** query requires Noting, produces LocalDate */
    val q: Query[Void, LocalDate] = fr.query(date)

    session.use { s: Session[IO] =>
      val x: IO[LocalDate] = s.unique(q)
      x
    }.log

  }

  test("2. simple query. Many results") {
    val fr: Fragment[Void] = sql"select name from country"
    val q: Query[Void, String] = fr.query(varchar) // text vs varchar - DIFFERENT

    session.use { s: Session[IO] =>
      val x = s.execute(q)
      x
    }.log

  }

  test("3. return tuple") {
    val q: Query[Void, String ~ Int] =
      sql"""
        SELECT name, population
        FROM country
      """.query(varchar ~ int4)

    session.use(_.execute(q)).log
  }

  object c {
    case class Country(name: String, population: Int)

    val countryDecoder1: Decoder[Country] = (varchar ~ int4).map { case (n, p) => Country(n, p) }
    val countryDecoder2: Decoder[Country] = (varchar *: int4).to[Country]
  }

  test("4. return case class") {
    val q: Query[Void, c.Country] = sql"""
        SELECT name, population
        FROM country
      """.query(c.countryDecoder1)

    session.use(_.execute(q)).log
  }

  test("5. twiddle based decoder") {
    val q: Query[Void, c.Country] = sql"""
        SELECT name, population
        FROM country
      """.query(c.countryDecoder2)

    session.use(_.execute(q)).log
  }

  /** Session has:
    * - s.execute 0..N
    * - s.unique  1
    * - s.option  0/1
    */

  test("6. passing parameters") {
    val q: Query[String, c.Country] =
      sql"""
        SELECT name, population
        FROM   country
        WHERE  name LIKE $varchar
      """.query(c.countryDecoder2)

    session.use(_.execute(q)("U%")).log
  }

  test("7. preparing and streaming") {
    val q: Query[String, c.Country] =
      sql"""
        SELECT name, population
        FROM   country
        WHERE  name LIKE $varchar
      """.query(c.countryDecoder2)

    session.use { s: Session[IO] =>
      s.prepare(q)
        .flatMap { pq =>
          pq.stream("U%", 64)
            .evalMap(logF)
            .compile
            .drain
        }
    }
  }

  test("8. passing namy parameters: String + Int") {
    val q: Query[String *: Int *: EmptyTuple, c.Country] =
      sql"""
        SELECT name, population
        FROM   country
        WHERE  name LIKE $varchar
        AND    population < $int4
    """.query(c.countryDecoder2)

    session.use { s: Session[IO] =>
      s.prepare(q)
        .flatMap { pq =>
          val args = ("U%", 30_000_000)

          pq.stream(args, 64)
            .evalMap(logF)
            .compile
            .drain
        }
    }
  }

  /** PreparedQuery has:
    * - pq.stream 0..N
    * - pq.option 0/1
    * - pq.unique 1
    * - pq.cursor (pages)
    * - pq.pipe
    */

  test("9. commands") {
    val a: Command[Void] = sql"""
      SET seed TO 0.123
    """.command

    session.use(_.execute(a))
  }

  test("10. prepare statements") {
    val c: Command[String] =
      sql"""
        DELETE FROM country WHERE name = $varchar
      """.command

    session.use { s =>
      s.prepare(c)
        .flatMap { pc =>
          List("qwe", "asd", "zxc")
            .traverse(pc.execute)
        }
    }
  }

  test("11. streaming") {
    val c: Command[String] =
      sql"""
        DELETE FROM country WHERE name = $varchar
      """.command

    session.use { s =>
      fs2.Stream.eval(s.prepare(c))
        .flatMap { pc =>
          fs2.Stream("qwe", "asd", "zxc")
            .through(pc.pipe)
        }
        .compile
        .drain
    }
  }

  test("12. contramapping parameters") {
    val update1: Command[String *: String *: EmptyTuple] =
      sql"""
        UPDATE country
        SET    headofstate = $varchar
        WHERE  code = ${bpchar(3)}
      """.command

    case class Info(code: String, hos: String)

    val update2: Command[Info] = update1.contramap { case Info(code, hos) => code *: hos *: EmptyTuple }
    // or
    val update3: Command[Info] = update1.to[Info]
  }

  test("13. list of parameters") {
    // derive encoder for list of length 3
    val xs: Encoder[List[String]] = varchar.list(3)

    // DELETE FROM country WHERE name IN ($1, $2, $3)
    val delete3: Command[List[String]] =
      sql"""
        DELETE FROM country WHERE name IN ($xs)
      """.command

    pprint.log(delete3.sql)

    session.use(_.execute(delete3)(List("A", "B", "C")))
  }

  test("14. bulk insert") {
    def insertMany(xs: List[(String, Short)]): Command[List[(String, Short)]] = {
      val encoder: Encoder[List[(String, Short)]] = (varchar ~ int2).values.list(xs.length)
      sql"INSERT INTO pets VALUES $encoder".command
    }

    val xs = List[(String, Short)](
      "a" -> 1,
      "b" -> 3,
      "c" -> 5
    )

    val q = insertMany(xs)

    log(q.sql)

    session.use(_.execute(q)(xs))
  }

  test("15. transactions".only) {
    case class Pet(name: String, age: Short)

    val selectAllQ: Query[Void, Pet] =
      sql"SELECT name, age FROM pets"
        .query(varchar *: int2)
        .to[Pet]

    val insertOneQ: Command[Pet] =
      sql"INSERT INTO pets VALUES ($varchar, $int2)"
        .command
        .to[Pet]

    def selectAll(s: Session[IO]): IO[List[Pet]] =
      s.execute(selectAllQ)

    case class RxWithContext(a: Pet, t: Throwable) extends RuntimeException

    def tryInsertAll(s: Session[IO], xs: List[Pet]): IO[Either[(Throwable, String, Pet), Unit]] =
      s.prepare(insertOneQ)
        .flatMap { pc =>
          s.transaction.use { xa =>
            xa.savepoint
              .flatMap { sp =>
                xs.traverse_(x => pc.execute(x).adaptError(t => RxWithContext(x, t)))
                  .map(_.asRight)
                  .recoverWith {
                    case RxWithContext(x, ss @ SqlState.UniqueViolation(ex)) =>
                      val msg = ex.constraintName.getOrElse("<unknown>")
                      logF(s"Unique violation: $msg, for id=${x.age}, rolling back...") >>
                        xa.rollback(sp) >>
                        (ss, msg, x).asLeft.pure[IO]
                  }
              }
          }
        }

    /*  create table pets (
          name varchar,
          id   integer not null constraint pets_pk primary key
        );
     */
    val pets = List(
      Pet("Alice", 1),
      Pet("Tasker", 2),
      Pet("Jack", 3),
    )

    session
      .use(s => tryInsertAll(s, pets))
      .log

  }

}
