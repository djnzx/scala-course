package topics.implicits

/**
  * 1. directly specified
  * 2. locally defined
  * 3. from complement objects
  * 4. from manual imports
  */
object ImplicitScopesApp extends App {

  object X {
    implicit val y1 = 1
  }

  trait X {
    implicit val y1 = 3
  }

  class XA extends X {
//    import X.y1
//    implicit val y1 = 2
    def add(x: Int)(implicit y: Int) = x + y
    def print = println(add(5))
  }


  val x = new XA
  x.print
}
