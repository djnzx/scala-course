package doobiex

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobiex.Db.{driver, pass, url, user}

object Doobie4 extends IOApp.Simple {

  val xa = Transactor.fromDriverManager[IO](driver, url, user, pass)

  def run: IO[Unit] =
    sql"select name, age from students"
      .query[(String, Int)]
      .stream
      .transact(xa)
      .evalMap(x => IO(println(x)))
      .compile
      .drain

}
