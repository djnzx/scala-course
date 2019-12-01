package x71cats

object C065Imap extends App {

  final case class Box[A](value: A)

  trait Codec[A] {
    def encode(value: A): String
    def decode(value: String): A
    def imap[B](dec: A => B, enc: B => A): Codec[B] = {
      val self = this
      new Codec[B] {
        override def encode(value: B): String = self.encode(enc(value))
        override def decode(value: String): B = dec(self.decode(value))
      }
    }
  }

  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)

  implicit val stringCodec: Codec[String] = new Codec[String] {
    def encode(value: String): String = s"<$value>"
    def decode(value: String): String = value.substring(1, value.length-1)
  }

  implicit val intCodec:     Codec[Int]     = stringCodec.imap[Int]    (_.toInt,     _.toString)
  implicit val booleanCodec: Codec[Boolean] = stringCodec.imap[Boolean](_.toBoolean, _.toString)
  implicit val doubleCodec:  Codec[Double]  = stringCodec.imap[Double] (_.toDouble,  _.toString)
  implicit def boxCodec[A](implicit c: Codec[A]):
                             Codec[Box[A]]  =           c.imap[Box[A]] (Box(_),      _.value)

  println(encode("HELLO"))
  println(encode(Box(true)))
  println(decode[String]("<HELLO>"))
  println(decode[Box[Boolean]]("<true>"))
}
