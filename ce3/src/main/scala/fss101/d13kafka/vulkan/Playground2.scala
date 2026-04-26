package fss101.d13kafka.vulkan

import org.apache.avro.Schema
import org.apache.avro.io.DecoderFactory
import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite

class Playground2 extends AnyFunSuite with Inside {

  import _root_.vulcan._
  import _root_.vulcan.generic._

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

  // (300000000,80 8C 8D 9E 02)
  // (300000001,82 8C 8D 9E 02)
  // (300000002,84 8C 8D 9E 02)
  test("1") {
    case class A(x: Long)
    implicit val cA: Codec[A] = Codec.derive

    ((1 to 128)++(300_000_000 to 300_000_002)).foreach { x =>
      inside(
        Codec.toBinary(A(x))
      ) {
        case Right(a) => println(x -> a.hex)
      }
    }
  }

  test("2") {
    case class B(x: String)
    implicit val cB: Codec[B] = Codec.derive

    val s: Schema = Codec[B].schema.getOrElse(???)
    println(s.toString)


    inside(
      Codec.toBinary(B("abc"))
    ) {
      case Right(a) => println(a.hex)
    }
  }

  test("3") {
    case class B(x: String)
    implicit val cB: Codec[B] = Codec.derive

    val raw: Array[Byte] = Array(0x06, 0x61, 0x62, 0x63).map(_.toByte)
    val schemaStr =
      """
        |{
        |  "type":"record",
        |  "name":"B",
        |  "namespace":"fss101.d13kafka.vulkan.Playground2.<local Playground2>",
        |  "fields":[{"name":"x","type":"string"}]
        |}
        |""".stripMargin

    val writerSchema = new Schema.Parser().parse(schemaStr)
    val result = Codec.fromBinary[B](raw, writerSchema)
    println(result) // Right(B(abc))
  }

  test("4") {
    val raw: Array[Byte] = Array(0x06, 0x61, 0x62, 0x63).map(_.toByte)
    val schemaStr =
      """
        |{
        |  "type":"record",
        |  "name":"B",
        |  "fields":[{"name":"x","type":"string"}]
        |}
        |""".stripMargin

    val schema = new Schema.Parser().parse(schemaStr)
    val reader  = new org.apache.avro.generic.GenericDatumReader[org.apache.avro.generic.GenericRecord](schema)
    val df: DecoderFactory = org.apache.avro.io.DecoderFactory.get()
    val decoder = df.binaryDecoder(raw, null)
    val q1  = reader.read(null, decoder)
    val record  = reader.read(null, decoder)
    println(record)        // {"x": "abc"}
    println(record.get("x")) // abc
  }

  test("5"){

    val bb: Array[Byte] = Codec.toBinary("aп").getOrElse(???)
    println(bb.hex)
    // 06 61 D0 BF
    // len 3
    //    "a" "п"
  }

  /*

  Primitive:
  Null | Boolean | Int | Long | Float | Double | Bytes | String

  Constructors:
    Product  (Record)
    Sum      (Union)
    Sequence (Array)
    Map      (String → T)
    Enum     (Int index)
    Fixed    (N bytes)


   */


  test("6") {
    val bytes: Array[Byte] = Array(0x84, 0x8C, 0x8D, 0x9E, 0x02).map(_.toByte)
    val offset = 0
    val decoder = DecoderFactory.get().binaryDecoder(bytes, offset, bytes.length - offset, null)

    val value = decoder.readInt()
    decoder.readLong()

    decoder.readEnum() // int
    decoder.readIndex() // int

    decoder.readFloat()
    decoder.readDouble()

    decoder.readString()
    decoder.readNull()
    decoder.readBoolean()

    decoder.readBytes(???)
    decoder.readFixed(???)

    decoder.readArrayStart()
    decoder.readMapStart()

    pprint.log(value)
  }

}
