package doobiex

import cats.effect._
import cats.effect.implicits._
import cats.implicits._
import doobie.implicits._
import doobie.util.fragment
import doobie.util.fragments._

object Doobie5 extends IOApp.Simple {

  def app(ids: List[Int]) = {
    val inFra = ids.toNel.map(in(fr"id", _))
    val fr: fragment.Fragment = fr"select id, name from t1" ++ whereAndOpt(inFra)

    pprint.pprintln(fr)

    fr
      .query[(Int, String)]
      .to[List]
  }

  override def run: IO[Unit] =
    app(List(5, 6, 7, 8))
      .transact(xa[IO])
      .flatMap(
        _.traverse_ { case (id, name) =>
          IO(println(s"id: $id, name:$name"))
        },
      )
}
