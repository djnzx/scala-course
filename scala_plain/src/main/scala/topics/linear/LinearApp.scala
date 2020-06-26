package topics.linear

class A {
  def foo() = "A"
}

trait B extends A {
  override def foo() = "B" + super.foo()
}

trait C extends B {
  override def foo() = "C" + super.foo()
}

trait D extends A {
  override def foo() = "D" + super.foo()
}

object LinearApp extends App {
  val d = new A with D with C with B
  println(d.foo()) // CBDA
}
