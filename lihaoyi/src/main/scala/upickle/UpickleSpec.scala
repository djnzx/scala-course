package upickle

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import upickle.UpickleGuide.Person

/**
  * https://www.lihaoyi.com/upickle/
  */
class UpickleSpec extends AnyFunSpec with Matchers {
  describe("upickle") {
    import upickle.default._

    describe("case class") {
      val p = Person("Alex", 44)
      val EXP: String = """{"name":"Alex","age":44}"""
      
      it("serializes") {
        write(p) shouldEqual EXP
      }
      
      it("deserializes") {
        read[Person](EXP) shouldEqual p
      }
    }
    describe("basic types") {
      it("write") {
        write(1) shouldEqual "1"
        write(Seq(1, 2, 3)) shouldEqual "[1,2,3]"
        /** tuple as array */
        write((1, "omg", true)) shouldEqual """[1,"omg",true]"""
      }
      it("read") {
        read[Seq[Int]]("[1,2,3]") shouldEqual List(1, 2, 3)
        read[(Int, String, Boolean)]("""[1,"omg",true]""") shouldEqual (1, "omg", true)
      }
      it("more") {
        write(12: Int)       shouldEqual "12"
        write(12: Short)     shouldEqual "12"
        write(12: Byte)      shouldEqual "12"
        write(Int.MaxValue)  shouldEqual "2147483647"
        write(Int.MinValue)  shouldEqual "-2147483648"
        write(12.5f: Float)  shouldEqual "12.5"
        write(12.5: Double)  shouldEqual "12.5"
        // very long as strings 
        write(9223372036854775807L: Long) shouldEqual "\"9223372036854775807\""
        write(1.0/0: Double)              shouldEqual "\"Infinity\""
        write(Float.PositiveInfinity)      shouldEqual "\"Infinity\""
        write(Float.NegativeInfinity)      shouldEqual "\"-Infinity\""
      }
      it("formatted") {
        write(Array(1, 2, 3), indent = 4) shouldEqual
          """[
            |    1,
            |    2,
            |    3
            |]""".stripMargin
      }
      it("collections") {
        write(Seq(1, 2, 3))               shouldEqual "[1,2,3]"
        write(Vector(1, 2, 3))            shouldEqual "[1,2,3]"
        write(List(1, 2, 3))              shouldEqual "[1,2,3]"
        import collection.immutable.SortedSet
        write(SortedSet(1, 5, 3))         shouldEqual "[1,3,5]"
      }
      it("boolean") {
        write(true: Boolean)   shouldEqual "true"
        write(false: Boolean)  shouldEqual "false"
      }
      it("options as lists") {
        write(Some(1)) shouldEqual "[1]"
        write(None)    shouldEqual "[]"
      }
    }
    
    describe("messagePack") {
      it("1") {
        writeBinary(1)                                           shouldEqual Array(1)
        writeBinary(Seq(1, 2, 3))                                shouldEqual Array(0x93.toByte, 1, 2, 3)
        readBinary[Seq[Int]](Array[Byte](0x93.toByte, 1, 2, 3))  shouldEqual List(1, 2, 3)
        val serializedTuple = Array[Byte](0x93.toByte, 1, 0xa3.toByte, 111, 109, 103, 0xc3.toByte)
        writeBinary((1, "omg", true))                            shouldEqual serializedTuple
        readBinary[(Int, String, Boolean)](serializedTuple)      shouldEqual (1, "omg", true)
      }
    }
  }
}
