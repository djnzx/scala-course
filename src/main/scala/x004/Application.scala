package x004

object Application extends App {
  val p = new Person("Alex", "Rykh")
  p.printFullName
  p.firstName = "AlexR"
  println(p.firstName)
  p.age
//  p.lastName_$eq("RYKH")
  p.lastName = "rykh"
  p.printFullName

  new Pizza("BIG", 30)
  new Pizza("BIG")
  new Pizza(30)
  new Pizza(List("Small", "Smart", "Tasty"))
  new Pizza(size = 11, name = "Special")
  val p3 = Pizza("Cool", 7)
  val p4 = Pizza() // thankfully to companion Object
  //val o = new OnlyOne // breaks because of private
  val o = OnlyOne.instance

}
