package mono101

import monocle.{Lens, Optional}

object FAQ1 extends App {

  import monocle.Iso

  val m = Map("one" -> 1, "two" -> 2)

  val root: Iso[Map[String, Int], Map[String, Int]] = Iso.id[Map[String, Int]]
  val o1: Optional[Map[String, Int], Int] = root.index("two")
  val o2: Map[String, Int] => Map[String, Int] = o1.replace(0)
  val o3: Map[String, Int] = o2(m)

  pprint.pprintln(o3)

  val opt2: Optional[Map[String, Int], Int] = root.index("two")
  val opt3: Optional[Map[String, Int], Int] = root.index("three")
  // replace works like map A=>A, applies if present
  opt2.replace(0)(m) // update -- value at index "two" -> 0 (because index "two" exists)
  opt3.replace(3)(m) // noop -- value at index "three" - doesn't

  val len2: Lens[Map[String, Int], Option[Int]] = root.at("two")
  val len3: Lens[Map[String, Int], Option[Int]] = root.at("three")
  // replace works like modify Option[A]=>Option[A]




  // res1: Map[String, Int] = Map("one" -> 1, "two" -> 2) // noop because m doesn't have a value at "three"
  root.at("three").replace(Some(3))(m) // insert element at "three"
  // res2: Map[String, Int] = Map("one" -> 1, "two" -> 2, "three" -> 3)  // insert element at "three"
  root.at("two").replace(None)(m) // delete element at "two"
  // res3: Map[String, Int] = Map("one" -> 1)       // delete element at "two"
  root.at("two").replace(Some(0))(m) // upsert element at "two"
  // res4: Map[String, Int] = Map("one" -> 1, "two" -> 0)

}
