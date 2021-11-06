package essential

object X231Equality extends App {
  // given:
  final case class Person(name: String, email: String)

  // I want to implement "special" equality (only by emails, for example)
  val p1 = Person("alex1","a1@b.c")
  val p2 = Person("alex2","a1@b.c")

  println(p1 == p2) // false

  // step 1. declare interface
  trait Equal[A] {
    def equal(one: A, two: A): Boolean
  }

  // step 2. implement interface for our type
//  implicit
  val eq_email: Equal[Person] = new Equal[Person] {
    override def equal(one: Person, two: Person): Boolean = one.email == two.email
  }

  // now we can use it, but ugly, the same way as in the Java
  println(eq_email.equal(p1, p2)) // true

  // step 3. declaring implicit (can be joined with step 2, uncomment line 19)
  implicit val eq_email_i: Equal[Person] = eq_email

  // step 4. enrich Person with new method
  implicit class EqualPerson(first: Person) {
    def ===(second: Person)(implicit instance: Equal[Person]): Boolean = instance.equal(first, second)
  }
  // use it!
  println(p1.===(p2)) // true
  // say thanks to infix !
  println(p1 === p2)  // true
}
