package catsx.c062contra

object C062ContraMap extends App {

  trait Printable[A] { self =>
    def format(va: A): String
    // we pass the function which is aware og "unboxing"
    def contramap[B](func: B => A): Printable[B] = new Printable[B] {
      override def format(vb: B): String = self.format(func(vb))
    }
  }

  // Printable[String]
  implicit val str: Printable[String] = (value: String) => s"`$value`"
  // Printable[Boolean]
  implicit val bool: Printable[Boolean] = (value: Boolean) => if (value) "yes" else "no"

  // Printable[Box[A]] - naive implementation
  def boxNaive[A]: Printable[Box[A]] = (va: Box[A]) => s"${va.value}"

  // Printable[Box[A]] - smart - based on already existed Printable[A]
  implicit def boxContramap[A](implicit p: Printable[A]): Printable[Box[A]] = p.contramap[Box[A]](b => b.value)

  //////////////////////////////////////////////////////////////
  def formatx[A](value: A)(implicit pa: Printable[A]): String = pa.format(value)

  val s1: String = formatx("hello") // "hello"
  val s2: String = formatx(true) // yes
  val s3: String = formatx(Box(false)) // no
  val s4: String = formatx(Box("abc")) // "abc"
  println(s1)
  println(s2)
  println(s3)
  println(s4)
}
