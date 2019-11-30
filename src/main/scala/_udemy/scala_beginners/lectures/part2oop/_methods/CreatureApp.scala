package _udemy.scala_beginners.lectures.part2oop._methods

object CreatureApp extends App {

  class Creature(val name: String) {
    def +(who: Creature):String = s"$name __ ${who.name}"
    def unary_+ = s"$name + EXTRA"
    def unary_! = s"!!! + $name"
  }

  val c1 = new Creature("C1")
  val c2 = new Creature("C2")
  val c3 = c1 + c2
  println(c3)
  println(+ c1)
  println(! c2)
}
