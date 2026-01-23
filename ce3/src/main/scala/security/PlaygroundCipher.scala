package security

import java.nio.charset.Charset
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.Signature
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import org.scalatest.funsuite.AnyFunSuite
import scala.jdk.CollectionConverters.SetHasAsScala

class PlaygroundCipher extends AnyFunSuite {

  def hex(bytes: Array[Byte]) = bytes.map(b => "%02X".format(b)).mkString

  val data = "Hello".getBytes(Charset.forName("UTF-8"))

  test("key generation - symmetric") {
    val keyGenerator = KeyGenerator.getInstance("AES")

    val secureRandom = new SecureRandom
    val keyBitSize = 256
    keyGenerator.init(keyBitSize, secureRandom)

    val secretKey: SecretKey = keyGenerator.generateKey
    pprint.log(hex(secretKey.getEncoded))
  }

  test("key generation - asymmetric") {
    val secureRandom = new SecureRandom
    val keyPairGenerator = KeyPairGenerator.getInstance("DSA")
    val pair: KeyPair = keyPairGenerator.generateKeyPair
    val pvt = pair.getPrivate
    val pub = pair.getPublic
  }

  test("mac") {
    val mac = Mac.getInstance("HmacSHA256")
  }

  test("signature") {
    val signature = Signature.getInstance("SHA256WithDSA")
  }

  test("syntax") {
    // algorithm/mode/padding
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    pprint.log(cipher)
    val keyBytes = Array[Byte](0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    val algorithm = "RawBytes"
    val key = new SecretKeySpec(keyBytes, algorithm) // SecretKeySpec implements KeySpec, SecretKey
//    cipher.init(Cipher.ENCRYPT_MODE, key)
  }

  test("what's installed") {
    Security.getProviders
      .map(p => p -> p.getServices)
      .map { case (p, ss) => p -> ss.asScala.filter(_.getType == "Cipher").map(_.getAlgorithm) }
      .filter { case (p, ss) => ss.nonEmpty }
      .foreach(x => pprint.log(x))
  }

}
