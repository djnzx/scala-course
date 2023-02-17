package vigil_test

import cats.implicits._

object Logic extends App {

  def process(xs: Seq[(Int, Int)]) = {
    xs.groupMap { case (_, v) => v } { case (_, v) => v }
      .filterNot { case (_, l) => l.size % 2 == 0 }
      .fmap(_.length)
      .headOption
      .map { case (v, _) => xs.head._1 -> v }
      .getOrElse(sys.error("wrong file content"))
  }

  val src = List(
    1 -> 22,
    1 -> 22,
    1 -> 22,
    1 -> 22,
    1 -> 34,
    1 -> 34,
    1 -> 35
  )

  pprint.pprintln(process(src))

}
