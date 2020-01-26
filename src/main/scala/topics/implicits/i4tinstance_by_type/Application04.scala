package topics.implicits.i4tinstance_by_type

object Application04 extends App {
  // 0. Given
  val f_int = () => 42
  val f_str = () => "Hello"
  val f_dbl = () => 3.14

  // 1. The Problem
  val a1: Int    = f_int()
  val a2: String = f_str()

  // The Scaffolding
  trait Read[A] {
    def read(): A
  }
  // full syntax
  implicit val int_impl: Read[Int] = new Read[Int] {
    override def read(): Int = f_int()
  }
  // shortened syntax, because of single method
  implicit val str_impl: Read[String] = () => f_str()
  // Access to particular instance BY TYPE, if we need
  val instance1: Read[Int]    = implicitly[Read[Int]]
  val instance2: Read[String] = implicitly[Read[String]]
  // The Solution #1
  def read1[A: Read]: A = implicitly[Read[A]].read()
  val w1 = read1[Int]
  val w2 = read1[String]
  val w3 = read1[Double](() => scala.math.Pi)

  // The Solution #2
  def read2[A](implicit instance: Read[A]): A = instance.read()
  // and use fluent approach
  val v1: Int    = read2[Int]
  val v2: String = read2[String]
  // if trait method in line #15 declared w/o parenthesis there is no way to use this syntax
  val v3: Double = read2[Double](() => scala.math.Pi)
  println(v1)
  println(v2)
  println(v3)
}
