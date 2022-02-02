package typemember

object TypeMemberExploration {

  // generic version
  trait DoSomething[A] {
    def get: A
  }

  // type member version
  trait DoSomething2 {
    type A
    def get: A
  }

  class Help extends DoSomething[Int] {
    override def get: Int = 1
  }

  class Kill extends DoSomething[String] {
    override def get: String = "Aha"
  }

  class Say extends DoSomething2 {
    override type A = Double
    override def get: A = 3.15
  }

  /** I can keep being abstract and not exposing too much details */
  abstract class Info extends DoSomething2

  val h = new Help
  val x1: Int = h.get

  val k = new Kill
  val x2: String = k.get

  val s = new Say
  val x3: Double = s.get

}
