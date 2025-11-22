package security

import java.nio.charset.Charset
import java.security.MessageDigest
import org.scalatest.funsuite.AnyFunSuite

class PlaygroundMessageDigest extends AnyFunSuite {

  def hex(bytes: Array[Byte]) = bytes.map(b => "%02X".format(b)).mkString

  val data = "Hello".getBytes(Charset.forName("UTF-8"))

  test("MD5, legacy, not secure") {
    val md = MessageDigest.getInstance("MD5")
    val hash = md.digest(data)
    val rep = hex(hash)
    pprint.log(hash.length) // 16
    pprint.log(rep)
  }

  test("SHA1, legacy, collision exist") {
    val md = MessageDigest.getInstance("SHA-1")
    val hash = md.digest(data)
    val rep = hex(hash)
    pprint.log(hash.length) // 20
    pprint.log(rep)
  }

  test("SHA-224") {
    val md = MessageDigest.getInstance("SHA-224")
    val hash = md.digest(data)
    val rep = hex(hash)
    pprint.log(hash.length) // 28
    pprint.log(rep)
  }

  test("SHA-256") {
    val md = MessageDigest.getInstance("SHA-256")
    val hash = md.digest(data)
    val rep = hex(hash)
    pprint.log(hash.length) // 32
    pprint.log(rep)
  }

  test("SHA-384") {
    val md = MessageDigest.getInstance("SHA-384")
    val hash = md.digest(data)
    val rep = hex(hash)
    pprint.log(hash.length) // 48
    pprint.log(rep)
  }

  test("SHA-512") {
    val md = MessageDigest.getInstance("SHA-512")
    val hash = md.digest(data)
    val rep = hex(hash)
    pprint.log(hash.length) // 64
    pprint.log(rep)
  }

  test("SHA-512/224") {
    val md = MessageDigest.getInstance("SHA-512/224")
    val hash = md.digest(data)
    val rep = hex(hash)
    pprint.log(hash.length) // 64 -> 28 (truncated, how?)
    pprint.log(rep)
  }

  test("SHA-512/256") {
    val md = MessageDigest.getInstance("SHA-512/256")
    val hash = md.digest(data)
    val rep = hex(hash)
    pprint.log(hash.length) // 64 -> 32 (truncated, how?)
    pprint.log(rep)
  }

}
