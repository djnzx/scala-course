package catsx

object C065ImapCodec extends App {

  final case class Box[A](value: A)

  trait Codec[A] {
    def encode(value: A): String
    def decode(raw: String): A
    def imap[B](f_dec: A => B, g_enc: B => A): Codec[B] = {
      val self = this
      new Codec[B] {
        override def encode(value: B): String = self.encode(g_enc(value))
        override def decode(raw: String): B = f_dec(self.decode(raw))
      }
    }
  }

  implicit val stringCodec: Codec[String] = new Codec[String] {
    def encode(value: String): String = s"<$value>"
    def decode(raw: String): String = raw.substring(1, raw.length-1)
  }

  implicit val intCodec:     Codec[Int]     = stringCodec.imap[Int]    (x => x.toInt,     x => x.toString)
  implicit val booleanCodec: Codec[Boolean] = stringCodec.imap[Boolean](x => x.toBoolean, x => x.toString)
  implicit val doubleCodec:  Codec[Double]  = stringCodec.imap[Double] (x => x.toDouble,  x => x.toString)
  implicit def boxCodec[A](implicit c: Codec[A]):
                             Codec[Box[A]]  =           c.imap[Box[A]] (x => Box(x),      x => x.value)

  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)

  // when we encode, type can be inferred from the argument provided
  // we DO NOT NEED to specify which type to decode
  println(encode(1))
  println(encode("HELLO"))
  println(encode(Box(true)))

  // when we decode, there is no way to infer type (argument is always string)
  // we NEED to specify which type to decode
  println(decode[String]("<HELLO>"))
  println(decode[Box[Boolean]]("<true>"))
  // one value can be decoded into different types
  println(decode[Box[Int]]("<1>"))
  println(decode[Int]("<1>"))
}
