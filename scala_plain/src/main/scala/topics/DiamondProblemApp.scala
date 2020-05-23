package topics

object DiamondProblemApp extends App {

  trait A {
    def a: Unit = println("A")
  }

  trait B {
    def a: Unit = println("B")
  }

  trait C {
    def a: Unit = println("C")
  }

  class D extends A with B with C {
    override def a: Unit = super[A].a
    def b: Unit = super[B].a
    def c: Unit = super[C].a
    def d = super.a // C, last joined
  }

  val d = new D
  d.a
  d.b
  d.c
  d.d

}
