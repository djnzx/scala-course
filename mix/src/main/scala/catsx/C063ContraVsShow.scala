package catsx

object C063ContraVsShow extends App {

  final case class Box[A](value: A)

  trait Printable[A] { me =>
    def format(a: A): String
    // we describe how to convert from any B to A
    // and result will be
    def contraMap[B](f: B => A): Printable[B] = new Printable[B] {
      override def format(b: B): String = {
        val a: A = f(b)
        val s: String = me.format(a)
        s
      }
    }
  }

  // Printable[Boolean]
  implicit val print_boolean: Printable[Boolean] = (a: Boolean) => if (a) "yes" else "no"
  // Printable[String]
  implicit val print_string: Printable[String] = (a: String) => s"`$a`"

  // that's the function which will convert the Box[A] to Printable[Box[A]
  def print_box_naive[A](implicit p: Printable[A]): Printable[Box[A]] = (a: Box[A]) => p.format(a.value)
  // but the problem of this approach, we need explicitly say what to do with each type
  // and do a lot of code duplication

  // syntax #1 plain
  def print_box0[A](implicit p: Printable[A]): Printable[Box[A]] = new Printable[Box[A]] {
    override def format(a: Box[A]): String = p.contraMap((ba: Box[A]) => ba.value).format(a)
  }

  // syntax #2 anonymous method
  def print_box1[A](implicit p: Printable[A]): Printable[Box[A]] =
    (a: Box[A]) => p.contraMap((ba: Box[A]) => ba.value).format(a)

  // syntax #3 using approach that function is a value
  implicit def print_box2[A](implicit p: Printable[A]): Printable[Box[A]] =
    p.contraMap((ba: Box[A]) => ba.value)

  //////////////////////////////////////////////////////////////
  // just usage
  //////////////////////////////////////////////////////////////
  def formatx[A](value: A)(implicit pa: Printable[A]): String = pa.format(value)

  val s1: String = formatx("hello") // "hello"
  val s2: String = formatx(true)    // yes
  val s3: String = formatx(Box(false))     // no
  val s4: String = formatx(Box("abc"))     // "abc"
  println(s1)
  println(s2)
  println(s3)
  println(s4)
}
