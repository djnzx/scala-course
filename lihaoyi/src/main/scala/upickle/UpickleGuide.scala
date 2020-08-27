package upickle

object UpickleGuide extends App {
  import upickle.default.{ReadWriter => RW, macroRW}
  // case class
  case class Person(name: String, age: Int)
  // object with implicit reader/writer
  object Person {
    implicit val rw: RW[Person] = macroRW
  }
  /** case class => JSON String */
  val json: String = upickle.default.write(Person("Alex", 44))
  println(json)
  /** JSON String => case class */
  val obj: Person = upickle.default.read[Person](json)
  println(obj)

}
