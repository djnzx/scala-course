package ext

import scala.util.Random

object ExtensionMethodOnCompanionObject extends App {

  case class Person(id: Int)
  object Person

  implicit class PersonOps(p: Person.type) {
    def next = Person((new Random).nextInt())
  }

  val p: Person = Person.next
  pprint.pprintln(p)

}
