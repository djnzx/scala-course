package doobiex

import cats.effect._
import cats.implicits._
import doobie.implicits._
import doobie.util
import doobie.util.fragment
import doobie.util.fragment.Fragment
import doobie.util.fragments.whereAndOpt

object Doobie5 extends IOApp.Simple {

  def in_[A: util.Put](field: String, values: List[A]) =
    values.toNel.map(doobie.util.fragments.in(Fragment.const(field), _))

  def app(ids: List[Int]) = {
    val inFra: Option[doobie.Fragment] = in_("id", ids)
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
