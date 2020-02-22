package aa_fp

object Fps112ConcurrencyFail extends App {

  class CPerson(var name: String, var city: String, var state: String) {
    override def toString: String = s"name: $name, city: $city, state: $state"
  }

  val person = new CPerson("Jim", "New-York", "NY")

  val t1 = new Thread {
    override def run(): Unit = {
      Thread.sleep(1000)
      person.name = "Joe"
      Thread.sleep(3000)
      person.state = "CO"
    }
  }

  t1.start()
  println(s"1. $person")
  Thread.sleep(2000)
  println(s"2. $person")
  Thread.sleep(2000)
  println(s"3. $person")
}
