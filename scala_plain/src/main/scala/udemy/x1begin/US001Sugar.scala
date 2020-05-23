package udemy.x1begin

object US001Sugar extends App {
  // naming
  class WeirdNaming(name: String) {
    def `and then say`(gossip: String) = println(s"$name said: $gossip")
  }

  val xen = new WeirdNaming("Xen");
  xen `and then say` "Hi!"

  class Composite[A,B]
//  val xc: Composite[String, Int] = ???
//  val yc: String Composite Int = ???
//
//  class -->[A, B]
//  val zc: Int --> String = ???

  val ana = Array(1,2,3)
  ana(2) = 7 // will be rewritten
  ana.update(2,7)

  class Mutable {
    private var enclosed: Int = 0
    def member: Int = enclosed // getter
    def member_=(value: Int): Unit = enclosed = value
  }

  val m: Mutable = new Mutable()
  m.member = 42     // setter
  val mv = m.member // getter
  println(mv)


}
