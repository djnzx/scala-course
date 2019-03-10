package x005

object TraitsNuances extends App {
  trait Human {
    def hello = "human"
  }
  trait Mother extends Human {
    override def hello = "mother"
  }
  trait Father extends Human {
    override def hello = "father"
  }
  class Child extends Human with Mother with Father {
    def helloSuper = super.hello
    def helloMother = super[Mother].hello
    def helloFather = super[Father].hello
    def helloHuman = super[Human].hello
    override def hello = "it's me!"
  }
  val c = new Child
  println(c.hello)
  println(c.helloFather)
  println(c.helloMother)
  println(c.helloHuman)
  println(c.helloSuper)
}
