package x99

import x99.Sex.Sex

object Sex extends Enumeration {
  type Sex = Value
  val male, female, third = Value
}

object PersonApp extends App {

  case class Person(name: String, age: Int, sex: Sex)
  val alex1 = Person("Alex", 50, Sex.male)
  val alex2 = Person("Alex", 50, Sex.male)
  val alex3 = alex2.copy(age = 51)

  println( alex1 equals alex2)
  println( alex2 )
  println( alex3 )
  println( Sex.values )

}

