package cats

object C062ContraMap extends App {

  final case class Box[A](value: A)

  trait Printable[A] { self =>
    def format(va: A): String
    def contramap[B](func: B => A): Printable[B] = new Printable[B] {
      override def format(vb: B): String = self.format(func(vb))
    }
  }

  // Printable[String]
  implicit val stringPrintable: Printable[String] = (value: String) => "\"" + value + "\""

  // Printable[Boolean]
  implicit val booleanPrintable: Printable[Boolean] = (value: Boolean) => if (value) "yes" else "no"

  // Printable[Box[A]] - naive implementation
  def boxPrintable_naive[A]: Printable[Box[A]] = (va: Box[A]) => "<" + va.value + ">"

  // Printable[Box[A]] - smart - based on already existed Printable[A]
  implicit def boxPrintable_contra[A](implicit p: Printable[A]): Printable[Box[A]] = p.contramap[Box[A]](_.value)

  def format[A](value: A)(implicit p: Printable[A]): String = p.format(value)

  val s1: String = format("hello") // "hello"
  val s2: String = format(true)    // yes
  val s3: String = format(Box(true))      // yes
  val s4: String = format(Box("abc"))     // "abc"
  println(s1)
  println(s2)
  println(s3)
  println(s4)
}
