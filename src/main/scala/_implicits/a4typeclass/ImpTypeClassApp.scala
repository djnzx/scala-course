package _implicits.a4typeclass

object ImpTypeClassApp extends App {

  // 0. Given
  val get_int = () => 142
  val get_str = () => "Hello"

  // 1. The Problem
  val a1: Int    = get_int()
  val a2: String = get_str()

  trait Reader[A] {
    def read: A
  }

  implicit val int_reader: Reader[Int] = new Reader[Int] {
    override def read: Int = get_int()
  }

  implicit val str_reader: Reader[String] = new Reader[String] {
    override def read: String = get_str()
  }

  // solution 1. better, but still bad.
  val b1: Int    = int_reader.read
  val b2: String = str_reader.read

  // solution 2. better, but we have to specify ClassName because function inside
  object Reader {
    def read[A](implicit instance: Reader[A]): A = instance.read
  }
  val c1: Int    = Reader.read[Int]
  val c2: String = Reader.read[String]
  println(c1)
  println(c2)

  // solution 3.1. instance chosen by 'implicitly[Reader[A]]' in compile time
  def zRead[A: Reader]: A = implicitly[Reader[A]].read
  val d1: Int    = zRead[Int]
  val d2: String = zRead[String]
  println(d1)
  println(d2)

  // solution 3.2. instance chosen by 'implicit instance via object' in compile time
  def zRead2[A: Reader]: A = Reader.read[A]
  val e1: Int    = zRead2[Int]
  val e2: String = zRead2[String]
  println(e1)
  println(e2)

}
