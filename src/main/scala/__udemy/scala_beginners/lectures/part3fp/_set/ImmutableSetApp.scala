package __udemy.scala_beginners.lectures.part3fp._set

object ImmutableSetApp extends App {
  val s1 = Set(1, 2) // [1,2]
  val s2 = s1 + 3 // [1,2,3]
  val s3 = s2 + (4, 5) // [1,2,3,4,5]
  val s4 = s3 ++ List(6, 7) // [1,2,3,4,5,6,7]

  val s5 = s4 - 1
  println(s5)
  val s6 = s5 - (7,8)
  println(s6)
  val s7 = s6 -- List(5,6,7)
  println(s7)
}
