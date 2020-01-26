package essential

import scala.collection.immutable

object X197Map extends App {
  val map1: Map[String, Int] = Map("a" -> 1, "b" -> 2, "c" -> 3)
  val map3: Map[String, Int] = Map("b" -> 2, "c" -> 3, "d" -> 4)
  val v1: Option[Int] = map1.get("a")   // option[value]
  val v2: Int = map1("a")               // value or exception (apply)
  val v3: Int = map1.getOrElse("a", 11) // value or default. signature: default: => V1. LAZY out of a box!
  map1.contains("a")
  val map2: Map[String, Int] = map1 - "c" + ("d" -> 14)
  val union: Map[String, Int] = map1 ++ map3 // Map(a -> 1, b -> 2, c -> 3, d -> 4)
  val mapWithDef = map1.withDefaultValue(-13)

  val mapMapped: immutable.Iterable[String] = map1.map(item => item._1 + " " + item._2)
  val mapFlatMapped1: Map[String, Int] = map1 flatMap {
    case (k, v) => (1 to 3).map(x => (k + x) -> (v * x))
  }
  // the same
  val mapFlatMapped2: Map[String, Int] = for{
    (str, num) <- map1
    x          <- 1 to 3
  } yield (str + x) -> (num * x)
  // a  -> 1, b  -> 2, c  -> 3
  // ->
  // a1 -> 1, a2 -> 2, a3 -> 3,
  // b1 -> 2, b2 -> 4, b3 -> 6,
  // c1 -> 3, c2 -> 6, c3 -> 9,
  println(mapFlatMapped2)

}
