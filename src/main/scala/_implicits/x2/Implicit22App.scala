package _implicits.x2

object Implicit22App extends App {

  // real hardcoded implementation
  val readString = () => "Hello"
  val readBoolean = () => true
  val readInt = () => 42
  val readDouble = () => 3.14
  val readFloat: () => Float = () => 5.17F

  // abstraction level we want to have
  trait IRead[A] {
    def access(): A
  }

  // implementations for future automatic substitution
  implicit val _r_string2: IRead[String]  = () => readString()
  implicit val _r_bool:    IRead[Boolean] = () => readBoolean()
  implicit val _r_double:  IRead[Double]  = () => readDouble()
  implicit val _r_int:     IRead[Int]     = () => readInt()

  // use Scala power to chose implementation appropriate/respectively
  def smart[A: IRead](): A = implicitly[IRead[A]].access()

  // we do have the implicit implementations in the scope for those types, we CAN use them!
  val iv1: Int     = smart[Int]()
  val iv2: String  = smart[String]()
  val iv3: Boolean = smart[Boolean]()
  val iv4: Double  = smart[Double]()
  // we dont't have implicit implementation in the scope for the type 'Float', so we MUST declare it right here
  // classic way
  val iv5: Float   = smart[Float]()( new IRead[Float] {
    override def access(): Float = readFloat()
  } )
  // short way (single method)
  val iv6: Float   = smart[Float]()( () => readFloat() )
  val iv7: Float   = smart[Float]()( () => 6.3F )
  println(s"$iv1\n$iv2\n$iv3\n$iv4\n$iv5\n$iv6\n$iv7")
}
