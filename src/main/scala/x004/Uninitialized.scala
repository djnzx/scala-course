package x004

object Uninitialized extends App {
  // approach 1 - set to Option
  var s1: Option[String] = None
  // approach 2 - default value
  var s2 = "default value"

  // we can run foreach on Option !
  // it doesn't crash on null values !
  s1.foreach(println)

  println(s1)
  println(s2)

}
