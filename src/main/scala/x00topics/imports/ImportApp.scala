package x00topics.imports

object ImportApp extends App {
  val s = new Library
  println(s.do1())
  println(s.do2)

  // use import in any place and even for importing public methods in local scope
  import s._
  println(do3)
  println(do4())

  println(do1() == s.do1())
}
