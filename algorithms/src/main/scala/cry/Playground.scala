package cry

import cry.Utils.{byteToHex, bytesToHex}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import java.security.MessageDigest
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/** https://www.manning.com/books/real-world-cryptography
  * https://mvnrepository.com/artifact/org.bouncycastle?sort=popular
  */

object Playground extends App {

  //  @throws[NoSuchAlgorithmException]
//  @throws[InvalidKeyException]
  def hmac(algorithm: String, data: String, key: String) = {
    val secretKeySpec = new SecretKeySpec(key.getBytes, algorithm)
    val mac           = Mac.getInstance(algorithm)
    mac.init(secretKeySpec)
    bytesToHex(mac.doFinal(data.getBytes))
  }

  val hm = hmac("HmacSHA256", "alex", "12")
  println(hm)

  val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
  val r: Array[Byte]        = digest.digest("abc".getBytes)
//  println(r.length)
  val r2                    = r.map(_.toHexString)
  val r3                    = r.map(byteToHex)
//  pprint.pprintln(r.mkString(" "))
//  pprint.pprintln(r2.mkString(" "))
  pprint.pprintln(r3.mkString(""))
//  pprint.pprintln(r)

  val enc: Base64.Encoder  = java.util.Base64.getEncoder
  val dec: Base64.Decoder  = java.util.Base64.getDecoder
  val encoded: Array[Byte] = enc.encode("qwe".getBytes)
  pprint.pprintln(new String(encoded))
  val decoded              = dec.decode(encoded)
  pprint.pprintln(new String(decoded))

//  javax.crypto.Cipher
}

/** https://www.scalatest.org/user_guide/property_based_testing
  * https://www.scalatest.org/scaladoc/plus-scalacheck-1.14/3.2.2.0/org/scalatestplus/scalacheck/ScalaCheckDrivenPropertyChecks.html
  * https://github.com/scalatest/scalatestplus-scalacheck
  */
class PlaygroundSpec extends AnyFunSpec with Matchers with ScalaCheckPropertyChecks {

  describe("tests") {

    it("1") {
      1 shouldEqual 1
    }

    it("nested forall count") {
      var c = 0
      forAll { s1: String => // 10 times

        forAll{ s2: String => // 10 times
          c += 1
          val s3 = s1 + s2
          s3.length should be >= 0
        }

      }
      pprint.pprintln(c) // 10x10 = 100 times
    }


    it("2") {

      forAll { (s1: String, s2: String) =>
        val sum = s1 + s2
        sum.contains("") shouldBe true
        sum.contains(s1) shouldBe true
        sum.contains(s2) shouldBe true
        sum.length should be >= 0
      }

    }

  }

}
