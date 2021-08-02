package catsx.c062contra

import catsx.c062contra.C065ImapCodec.Instances
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

/**
  * general idea behind the imap, that it is
  * just a combination of map and contramap
  * so we automatically derive instances by appending or prepending the chain
  */
object C065ImapCodec {

  /**
    * it doesn't cope with failures.
    * to deal with failures
    * decode must have signature:
    * def decode(raw: String): Either[Error, A]
    */
  trait Codec[A] {
    def encode(value: A): String
    def decode(raw: String): A
    def imap[B](dec: A => B, enc: B => A): Codec[B] = {
      val self = this
      new Codec[B] {
        override def encode(value: B): String = self.encode(enc(value))
        override def decode(raw: String): B = dec(self.decode(raw))
      }
    }
  }

  object Instances {
    implicit val stringCodec: Codec[String] = new Codec[String] {
      def encode(value: String): String = s"<$value>"
      def decode(raw: String): String = raw.substring(1, raw.length - 1)
    }

    implicit val intCodec: Codec[Int] = stringCodec.imap[Int](x => x.toInt, x => x.toString)
    implicit val booleanCodec: Codec[Boolean] = stringCodec.imap[Boolean](x => x.toBoolean, x => x.toString)
    implicit val doubleCodec: Codec[Double] = stringCodec.imap[Double](x => x.toDouble, x => x.toString)

    /** naive implementation without any markers */
    implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] = c.imap[Box[A]](x => Box(x), x => x.value)
  }

  object Implementation {
    def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
    def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)
  }

}

class C065ImapCodecSpec extends AnyFunSpec with Matchers {
  import Instances._
  import C065ImapCodec.Implementation._

  describe("derivations") {
    it("encoding") {
      /** during encoding, type can be inferred from the argument provided
        * we DO NOT NEED to specify which type to decode
        */
      encode(1) shouldEqual "<1>"
      encode("HELLO") shouldEqual "<HELLO>"
      encode(Box(true)) shouldEqual "<true>"
    }

    /** during decoding, we MUST specify which type decode to
      * there is no way to infer type (argument is always string)
      */
    it("decoding #1") {
      decode[String]("<HELLO>") shouldEqual "HELLO"
      decode[Box[Boolean]]("<true>") shouldEqual Box(true)
    }

    /** due to our naive Box implementation
      * same data can be decoded to different values
      */
    it("decoding #2") {
      decode[Box[Int]]("<1>") shouldEqual Box(1)
      decode[Int]("<1>") shouldEqual 1
    }
  }
}
