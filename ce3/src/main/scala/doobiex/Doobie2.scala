package doobiex

import cats.data.NonEmptyList
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._

object Doobie2 extends IOApp {

  val program1 = sql"select name from t1"
    .query[String]
    .to[List] // CanBuildFrom

  val program2 = sql"select name from t1"
    .query[String]
    .stream
    // if we need client side filtering
    .take(2)
    .compile
    .toList

  /** plain tuple mapping */
  val program3 = sql"select id, name, qty from t1"
    .query[(Int, String, Option[Int])]
    .to[List]

  case class Row(id: Int, name: String, qty: Option[Int])

  /** case class mapping */
  val program3t = sql"select id, name, qty from t1 order by id"
    .query[Row]
    .to[List]

  /** any mix mapping */
  val program3x = sql"select id, name, qty from t1 order by id"
    .query[(Int, (String, Option[Long]))]
    .to[List]

  /** any mix mapping */
  val program3y = sql"select id, name, qty from t1 order by id"
    .query[(Int, (String, Option[Long]))]
    .stream
    .compile
    .toList
    .map(_.toMap)

  val program4 = (id: Int) =>
    sql"""
       select id, name, qty
       from t1
       where id=$id
       order by id
       """
      .query[(Int, (String, Option[Long]))]
      .to[List]

  case class MinMax(min: Int, max: Int)
  // program4b(MinMax(2,4))
  val program4b = (mm: MinMax) =>
    sql"""
       select id, name, qty
       from t1
       where id>=${mm.min}
         and id<=${mm.max}
       order by id
       """
      .query[(Int, (String, Option[Long]))]
      .to[List]

  val program4c = (ids: NonEmptyList[Int]) =>
    (
      sql"""
       select id, name, qty
       from t1
       """ ++
        Fragments.in(fr"where id", ids) ++
        sql"""
           order by id
         """
    )
      .query[(Int, (String, Option[Long]))]
      .to[List]

  import fs2.Stream
  type RR = (Int, (String, Option[Long]))

  val q: String = """
       select id, name, qty
       from t1
       where id >= ?
         and id <= ?
       order by id
       """

  // sql is just a wrapper over the stream and binding
  def proc(mm: MinMax): Stream[ConnectionIO, RR] =
    HC.stream[RR](q, HPS.set((mm.min, mm.max)), 512)

  val pst1: PreparedStatementIO[Unit] = HPS.set((10, 20)) // stmt setting two values
  val pst2: PreparedStatementIO[Unit] =
    HPS.set(1, 10) *> HPS.set(2, "Jim") *> HPS.set(3, true) // set values to specific indexes
  val pst3: PreparedStatementIO[Unit] = FPS.setString(3, "foo") *> FPS.setBoolean(1, true)

  val program5 = proc(MinMax(2, 3))
    .compile
    .toList

  val program = program5

  val rIO = program.transact(xa[IO])

  override def run(args: List[String]): IO[ExitCode] = for {
    rI <- rIO
    rs = rI.mkString("\n")
    _ <- putStrLn(rs)
  } yield ExitCode.Success

}
