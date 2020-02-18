package book_red

object Red003 extends App {
  // validation
  case class Person(name: Name, age: Age)
  sealed case class Name(name: String)
  sealed case class Age(age: Int)

  def mkName(name: String): Either[String, Name] =
    if (name == null || name == "") Left("Name is empty") else Right(Name(name))
  def mkAge(age: Int): Either[String, Age] =
    if (age < 0) Left("Age < 0") else Right(Age(age))
  def mkPerson(name: String, age: Int): Either[String, Person] = for {
    name <- mkName(name)
    age  <- mkAge(age)
  } yield Person(name, age)

  println(mkPerson("Alex", 44))
}
