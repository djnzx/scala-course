package _implicits.x2

object Implicit21App extends App {

  // real hardcoded implementation
  val readString = () => "Hello"
  val readBoolean = () => true
  val readInt = () => 42
  val readDouble = () => 3.14
  val readFloat = () => 5.17F

  // abstraction level we want to have
  trait IRead[A] {
    def access(): A
  }

  // complement object to build them in easy way
  object IRead {
    def apply[A](f: () => A): IRead[A] = () => f()
  }

  // implementations, which will be used implicitly
  // 1. full classic way
  implicit val _r_string1: IRead[String]  = new IRead[String] {
    override def access(): String = readString()
  }
  // 2. simplified way, because only one method
//  implicit val _r_string2: IRead[String]  = () => readString()
  // 3. classic way by using complement object to build
//  implicit val _r_string3: IRead[String]  = IRead[String](() => readString())
  implicit val _r_bool:    IRead[Boolean] = IRead[Boolean](() => readBoolean())
  implicit val _r_double:  IRead[Double]  = IRead[Double](() => readDouble())
  implicit val _r_int:     IRead[Int]     = IRead[Int](() => readInt())

  // use Scala power!
  def smart[A: IRead](): A = implicitly[IRead[A]].access()

  // we do have the implicit implementations in the scope for those types, we CAN use them!
  val iv1: Int     = smart[Int]()
  val iv2: String  = smart[String]()
  val iv3: Boolean = smart[Boolean]()
  val iv4: Double  = smart[Double]()
  // we dont't have implicit implementation in the scope for the type 'Float', so we MUST declare it right here
  val iv5: Float   = smart[Float]()( () => readFloat() )
  val iv6: Float   = smart[Float]()( () => 6.3F )
  println(s"$iv1\n$iv2\n$iv3\n$iv4\n$iv5\n$iv6")
//  println(readString) // function that returns 'String'
//  println(readString()) // result of running function
}
