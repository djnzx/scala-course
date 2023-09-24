package interview.general

object TTT {

  trait A {
    def a1: Int
    def a2: String
  }

  new A {
    override val a1: Int = 1
    override val a2: String = "hello"
  }

  class C(val a1: Int, val a2: String) extends A {
    def m1: Int = ???
  }
  object C {
    def apply(a1: Int, a2: String) = new C(a1, a2)
  }


  new C(1, "hello")
  C.apply(1, "hello")
  C(1, "hello")

  val f = new Function[Int, String] {
    override def apply(v1: Int): String = v1.toString
  }

  f.apply(3)
  f(3)

  val a: Array[Int] = Array(1,2,3,4,5)
  a.apply(2)
  a(2)

  "hello".apply(3)
  "hello"(3)







}
