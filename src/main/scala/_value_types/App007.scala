package _value_types

object App007 extends App {

  case class PersonInt(x: Int) extends AnyVal
  case class BookInt(x: Int) extends AnyVal

  val person1a = PersonInt(1)
  val person1b = PersonInt(1)
  val book1 = BookInt(1)

  if (person1a == person1b) println("Comparing similar types.... OK")

  if (person1a == book1) println("Different types") // false
  if (person1a.x == 1) println("Comparing Typed (with unboxing) an Untyped... OK")
  if (book1.x == 1) println("Comparing Typed (with unboxing) an Untyped... OK")
}
