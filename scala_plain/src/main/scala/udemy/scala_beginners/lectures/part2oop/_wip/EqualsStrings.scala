package udemy.scala_beginners.lectures.part2oop._wip

object EqualsStrings extends App {
  val s1 = "ABC"
  val s2 = "ABC"

  println(s1 equals s2) // embedded
  println(s1 == s2) // shortcut to equals
  println(s1 eq s2) // mem pointers
}
