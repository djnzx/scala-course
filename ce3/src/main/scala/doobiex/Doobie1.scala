package doobiex

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._

object Doobie1 extends IOApp {

  /** program1, value lifter to the context */
  val program1: ConnectionIO[Int] = 42.pure[ConnectionIO]

  /** program2, sql statement */
  val program2: ConnectionIO[Int] = sql"select 42".query[Int].unique

  /** program3, sequenced */
  val program3a: ConnectionIO[(Int, Double)] =
    for {
      a <- sql"select 42".query[Int].unique
      b <- sql"select random()".query[Double].unique
    } yield (a, b)

  val program3b = {
    val a: ConnectionIO[Int] = sql"select 42".query[Int].unique
    val b: ConnectionIO[Double] = sql"select random()".query[Double].unique
    (a, b).tupled
  }

  val program = program3b.replicateA(3)

  /** result */
  val rIO = program.transact(xa[IO])

  override def run(args: List[String]): IO[ExitCode] = for {
    rI <- rIO
    rs = rI.toString
    _ <- putStrLn(rs)
  } yield ExitCode.Success

}
