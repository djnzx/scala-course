package _twitter

object TWApp04 extends App {

  def method(x: { def print(m: String): Unit }, msg: String): Unit = {
    x.print(msg)
  }

  method(new Object {
    def print(m: String): Unit = {
      println(m)
    }
  }, "Hello")

  trait Z {
    type A
    val x: A
  }

  new Z {
    override type A = String
    override val x: A = "abc"
  }
  new Z {
    override type A = Int
    override val x: A = 123
  }

}
