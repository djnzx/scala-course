package x003

object P03_11PatternAdv extends App {
  val m1 = (origin: Any) => origin match {
    case l:List[_] => println(s"List[*] $l,\nwe don't know because of type erasure with generics")
    case _ => println("smth else")
  }

  m1(List(1,2,3))
  m1(List("Chrysler", "BMW", "Mercedes"))
  m1(List(true, false))

}
