package cookbook.x006

import java.io.IOException

object Casting extends App {
  case class Person(name: String)
  case class Teacher(person: Person, list: List[String])

  val teachers = new Array[Object](10)
  val p1 = Person("Alex")
  val t1 = Teacher(p1, List("math", "bio"))
  teachers(0) = t1
  val t0 = teachers(0).asInstanceOf[Teacher] // ClassCastException could be emerged
  println(t0)
  println(teachers(0).isInstanceOf[Person])
  println(teachers(0).isInstanceOf[Teacher])

  val l1: Long = 1111.asInstanceOf[Long]
  val javaClass = classOf[IOException]
  // javaClass: Class[java.io.IOException] = class java.io.IOException
  // this should be used when we interact with JAVA code where Class type is expected
  println(javaClass.getClass)
  echo(MAGIC)
}
