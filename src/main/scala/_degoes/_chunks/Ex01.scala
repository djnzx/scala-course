package _degoes._chunks

object Ex01 extends App {

  val f1 = () => "abc"
  val f2 = () => 123

  trait My[A] {
    def get: A
  }

  implicit val impl1: My[Int] = new My[Int] {
    override def get: Int = f2()
  }

  implicit val impl2: My[String] = new My[String] {
    override def get: String = f1()
  }

  def ff[A](implicit instance: My[A]): A = instance.get

  val v1: String = ff[String] // "abc
  val v2: Int    = ff[Int] // 123

  val f: Int => Int = (x: Int) => x + 1
  val g: Int => Int = (x: Int) => x * 2

  val h: Int => Int = f andThen g
  val h2: Int => Int = f compose g

}
