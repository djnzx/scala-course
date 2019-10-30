package _value_types

object App007 extends App {

  def doCheck(cond: Boolean): String = if (cond) "OK" else "ERR"

  case class PersonId(private val src: Int) extends AnyVal {
    def id = this.src // + 1
  }
  case class BookId(src: Int) extends AnyVal

  val just1   : Int       = 1
  val person1a: PersonId = PersonId(1)
  val person1b: PersonId = PersonId(1)
  val book1   : BookId   = BookId(1)

  val testData = List(
    "Similar Value types......................... %s" -> (person1a == person1b),
    "Different value types....................... %s" -> (person1a == book1),   // false
    "Value type vs Int........................... %s" -> (person1a == just1),   // false
    "Value type vs Literal....................... %s" -> (person1a == 1),       // false
    "Value type unboxed vs Literal............... %s" -> (book1.src == 1),
    "Value type extracted by getter vs Literal... %s" -> (person1a.id == 1),
    "Value type extracted by getter vs Int....... %s" -> (person1a.id == just1),
  )
  testData. foreach(el => printf(el._1+"\n", doCheck(el._2)))
}
