package fss101.d13kafka.vulkan

import org.scalatest.funsuite.AnyFunSuite

class Playground extends AnyFunSuite {

  import vulcan._

  implicit class ByteArrayOps(xs: Array[Byte]) {
    def hex: String = xs.map(b => "%02X".format(b)).mkString(" ")
  }

  implicit class IntOps(x: Int) {
    def toBytes: Array[Byte] = {
      def go(x: Int, acc: List[Byte]): List[Byte] = x match {
        case 0 => acc
        case x => go(x >> 8, (x & 0xff).toByte :: acc)
      }
      go(x, Nil).toArray
    }
  }

  test("0 - toBytes") {
    List(
      0,
      1,
      2,
      15,
      16,
      255,
      256,
      65534,
      65535,
      65536,
      5700,
      5700 << 1,
    ).foreach { x =>
      println("%5d".formatted(x) -> x.toBytes.hex)
    }
  }

  test("1 - show schema") {
    Codec[Car].schema.foreach(s =>
      println(s.toString(true))
    )
  }

  test("2 - encode to bytes") {
    val car = Car("Dodge", 255)
    //  1 -> 02
    //  2 -> 04
    //  4 -> 08
    //  8 -> 10
    // 16 -> 20
    // 15 -> 1E
    // 0A 44 6F 64 67 65 1E
    //     D  o  d  g  e 15
    Codec.toBinary(car).foreach { bytes =>
      println(bytes.hex)
    }
    // println(Array((15 << 1).toByte).hex) // 1E
  }

  test("2 - car encode to bytes") {
    val car = Car("A1", 1)
    // plain                          04 41 31 02
    // schema registry 00 00 00 00 01 04 41 31 02
    //                 00 00 00 00 01 04 41 32 04
    //                 00 00 00 00 01 04 41 33 06
    //                 ^  \________/  \_________/
    //                 |           \            \__ actual payload (provided by encoder)
    //                 |            \
    //                 |             \_____________ schema id
    //                 |
    //                 |___________________________ magic byte, always 00
    //
    Codec.toBinary(car).foreach { bytes =>
      println(bytes.hex)
    }
  }

  test("9") {
    Codec[Order].schema.foreach(s =>
      println(s.toString(true))
    )
  }

}
