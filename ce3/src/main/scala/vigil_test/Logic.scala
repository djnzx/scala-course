package vigil_test

import cats.implicits._

object Logic {

  /**
    * given Seq[Tuple2]
    * where
    * - 1st element is a key
    * - 2nd element ia s value
    *
    * returns Tuple2
    * where
    *  - 1st element is a key
    *  - 2nd element is a value, appeared odd number of times.
    * caveats:
    *  - takes 1st value appeared odd number of times
    *  - throws an exception if none of the values appeared odd number of times
    */
  def process(xs: Seq[(Int, Int)]) = {
    xs.groupMap { case (_, v) => v } { case (_, v) => v }
      .filterNot { case (_, l) => l.size % 2 == 0 }
      .fmap(_.length)
      .headOption
      .map { case (v, _) => xs.head._1 -> v }
      .getOrElse(sys.error("wrong file content"))
  }

}
