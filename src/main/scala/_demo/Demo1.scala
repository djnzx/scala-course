package _demo

object Demo1 extends App {
  def f1(a: Int)(implicit b: Int): Int = {
    a * b
  }

  implicit val five: Int = 5;
  val c1 = f1(1)
  val c2 = f1(2)
  val c3 = f1(3)

  implicit class SmartInt(origin: Int) {
    def inc = origin + 1
  }

  val six:Int = 5.inc

  def func_int = () => 5
  def func_str = () => "hello"

  trait Reader[T] {
    def read(): T
  }

  implicit val _iread: Reader[Int]    = () => func_int()
  implicit val _sread: Reader[String] = () => func_str()

  def func[T](implicit instance: Reader[T]): T = instance.read()

  val i1:Int    = func[Int]
  val s1:String = func[String]
  val f1:Double = func[Double]( () => Math.PI )

}
