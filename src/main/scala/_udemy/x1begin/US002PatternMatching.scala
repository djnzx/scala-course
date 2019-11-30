package _udemy.x1begin

object US002PatternMatching extends App {
  class Person (val name: String, val age: Int)
  object Person {
    def unapply(p: Person): Option[(String, Int)] = Some((p.name, p.age))
  }

  val a = List(1,2,3)
  a match {
    case 1 :: Nil   => println("List of(1) another syntax")                            // List(1)
    case List(1)    => println("List of(1)")                                           // List(1)
    case 1 :: List(_) => println("2 items another")
    case List(1, _) => println("List starting from 1 and contains only 2 items")       // List(1,2)
    case List(1, _*) => println("List starting from 1 and contains more than 2 items") // List(1,2,3)
    case _ => println("another")
  }

}
