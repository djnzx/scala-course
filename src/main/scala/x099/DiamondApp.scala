package x099

object DiamondApp extends App {
  trait A { def a = println("A") }
  trait B { def a = println("B") }
  trait C { def a = println("C") }

  class D extends A with B with C {
    override def a: Unit = super[A].a
    def b: Unit = super[B].a
    def c: Unit = super[C].a
  }

  val d = new D
  d.a
  d.b
  d.c

}
