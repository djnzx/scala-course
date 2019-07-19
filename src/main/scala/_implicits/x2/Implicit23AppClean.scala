package _implicits.x2

object Implicit23AppClean extends App {

  val readString = () => "Hello"
  val readBoolean = () => true
  val readInt = () => 42

  trait IRead[A] {
    def get(): A
  }

  implicit val _r_string2: IRead[String]  = () => readString()
  implicit val _r_bool:    IRead[Boolean] = () => readBoolean()
  implicit val _r_int:     IRead[Int]     = () => readInt()

  def smart[A: IRead](): A = implicitly[IRead[A]].get()

  val iv1: Int     = smart[Int]()
  val iv2: String  = smart[String]()
  val iv3: Boolean = smart[Boolean]()
  println(s"$iv1\n$iv2\n$iv3\n")
}
