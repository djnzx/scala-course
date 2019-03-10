package x004

object Application46 extends App {
  val p = new SmartPerson("Alex")
  println(p.name_)
  p.name__("ALEX")
  println(p.name_)
  println("----------")
  val p2 = new VerySmartPerson("Zino")
  println(p2.name)
  p2.name="ZZ"
  println(p2.name)
  p2.name_=("xx")
  println(p2.name)

  val s = new Stock
  println(s.delayedValue)

}
