package _tricky_import

object ImportApp extends App {
  val s = new Smart
  println(s.do1())
  println(s.do2)

  import s._
  println(do3)
  println(do4())

  println(do1() == s.do1())
}
