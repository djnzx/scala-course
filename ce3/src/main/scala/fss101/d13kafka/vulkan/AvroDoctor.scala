package fss101.d13kafka.vulkan

import org.scalatest.Inside
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import vulcan.Codec
import vulcan.generic._

sealed trait AvroValue
object AvroValue {
  final case class AvroInt(value: Int)         extends AvroValue
  final case class AvroLong(value: Long)       extends AvroValue
  final case class AvroFloat(value: Float)     extends AvroValue
  final case class AvroDouble(value: Double)   extends AvroValue
  final case class AvroBoolean(value: Boolean) extends AvroValue
  final case class AvroString(value: String)   extends AvroValue
}

object AvroDoctor {

  type Index = Int

  def readInt(bs: Array[Byte], idx: Index): Option[(Int, Index)] = {
    @annotation.tailrec
    def loop(i: Index, acc: Int, shift: Int): Option[(Int, Index)] =
      if (i >= bs.length) None
      else {
        val b = bs(i) & 0xff
        val next = acc | ((b & 0x7f) << shift)
        if ((b & 0x80) == 0) Some(((next >>> 1) ^ -(next & 1), i + 1))
        else loop(i + 1, next, shift + 7)
      }
    loop(idx, 0, 0)
  }

  def readLong(bs: Array[Byte], idx: Index): Option[(Long, Index)] = {
    @annotation.tailrec
    def loop(i: Index, acc: Long, shift: Int): Option[(Long, Index)] =
      if (i >= bs.length) None
      else {
        val b = bs(i) & 0xff
        val next = acc | ((b & 0x7fL) << shift)
        if ((b & 0x80) == 0) Some(((next >>> 1) ^ -(next & 1L), i + 1))
        else loop(i + 1, next, shift + 7)
      }
    loop(idx, 0L, 0)
  }

  def readFloat(bs: Array[Byte], idx: Index): Option[(Float, Index)] =
    if (idx + 4 > bs.length) None
    else {
      val bits = (bs(idx) & 0xff) |
        ((bs(idx + 1) & 0xff) << 8) |
        ((bs(idx + 2) & 0xff) << 16) |
        ((bs(idx + 3) & 0xff) << 24)
      Some((java.lang.Float.intBitsToFloat(bits), idx + 4))
    }

  def readDouble(bs: Array[Byte], idx: Index): Option[(Double, Index)] =
    if (idx + 8 > bs.length) None
    else {
      val bits = (bs(idx) & 0xffL) |
        ((bs(idx + 1) & 0xffL) << 8) |
        ((bs(idx + 2) & 0xffL) << 16) |
        ((bs(idx + 3) & 0xffL) << 24) |
        ((bs(idx + 4) & 0xffL) << 32) |
        ((bs(idx + 5) & 0xffL) << 40) |
        ((bs(idx + 6) & 0xffL) << 48) |
        ((bs(idx + 7) & 0xffL) << 56)
      Some((java.lang.Double.longBitsToDouble(bits), idx + 8))
    }

  def readBoolean(bs: Array[Byte], idx: Index): Option[(Boolean, Index)] =
    if (idx >= bs.length) None
    else Some((bs(idx) != 0, idx + 1))

  def readString(bs: Array[Byte], idx: Index): Option[(String, Index)] =
    readLong(bs, idx).flatMap { case (len, dataIdx) =>
      val end = dataIdx + len.toInt
      if (len < 0 || end > bs.length) None
      else Some((new String(bs, dataIdx, len.toInt, java.nio.charset.StandardCharsets.UTF_8), end))
    }

  def guess(bs: Array[Byte]): List[List[AvroValue]] = {
    import AvroValue._
    def go(idx: Index): List[List[AvroValue]] =
      if (idx == bs.length) List(Nil)
      else
        List(
          readInt(bs, idx).map { case (v, i) => (AvroInt(v), i) },
          readLong(bs, idx).map { case (v, i) => (AvroLong(v), i) },
//          readFloat(bs, idx).map   { case (v, i) => (AvroFloat(v),   i) },
//          readDouble(bs, idx).map  { case (v, i) => (AvroDouble(v),  i) },
//          readBoolean(bs, idx).map { case (v, i) => (AvroBoolean(v), i) },
          readString(bs, idx).map { case (v, i) => (AvroString(v), i) },
        ).flatten.flatMap { case (value, nextIdx) =>
          go(nextIdx).map(value :: _)
        }
    go(0)
  }

}

case class ABC(a: Int, b: String, c: Long, d: String)
object ABC {
  implicit val codec: Codec[ABC] = Codec.derive
}

class AvroDoctorSpec extends AnyFunSuite with Inside with Matchers {

  private def avroEncodeString(s: String): Array[Byte] = {
    val bos = new java.io.ByteArrayOutputStream()
    val enc = org.apache.avro.io.EncoderFactory.get().binaryEncoder(bos, null)
    enc.writeString(s); enc.flush(); bos.toByteArray
  }

  private def avroEncodeLong(n: Long): Array[Byte] = {
    val bos = new java.io.ByteArrayOutputStream()
    val enc = org.apache.avro.io.EncoderFactory.get().binaryEncoder(bos, null)
    enc.writeLong(n); enc.flush(); bos.toByteArray
  }

  private def avroEncodeBoolean(n: Boolean): Array[Byte] = {
    val bos = new java.io.ByteArrayOutputStream()
    val enc = org.apache.avro.io.EncoderFactory.get().binaryEncoder(bos, null)
    enc.writeBoolean(n); enc.flush(); bos.toByteArray
  }

  private def avroEncodeInt(n: Int): Array[Byte] = {
    val bos = new java.io.ByteArrayOutputStream()
    val enc = org.apache.avro.io.EncoderFactory.get().binaryEncoder(bos, null)
    enc.writeInt(n); enc.flush(); bos.toByteArray
  }

  private def avroEncodeFloat(n: Float): Array[Byte] = {
    val bos = new java.io.ByteArrayOutputStream()
    val enc = org.apache.avro.io.EncoderFactory.get().binaryEncoder(bos, null)
    enc.writeFloat(n); enc.flush(); bos.toByteArray
  }

  private def avroEncodeDouble(n: Double): Array[Byte] = {
    val bos = new java.io.ByteArrayOutputStream()
    val enc = org.apache.avro.io.EncoderFactory.get().binaryEncoder(bos, null)
    enc.writeDouble(n); enc.flush(); bos.toByteArray
  }

  // known byte sequences from the Avro spec (zig-zag + varint)
  test("readInt - spec examples") {
    AvroDoctor.readInt(Array(0x00.toByte), 0) shouldBe Some((0, 1))
    AvroDoctor.readInt(Array(0x01.toByte), 0) shouldBe Some((-1, 1))
    AvroDoctor.readInt(Array(0x02.toByte), 0) shouldBe Some((1, 1))
    AvroDoctor.readInt(Array(0x03.toByte), 0) shouldBe Some((-2, 1))
    // Int.MaxValue  → 0xFE 0xFF 0xFF 0xFF 0x0F
    AvroDoctor.readInt(Array(0xfe, 0xff, 0xff, 0xff, 0x0f).map(_.toByte), 0) shouldBe Some((Int.MaxValue, 5))
    // Int.MinValue  → 0xFF 0xFF 0xFF 0xFF 0x0F
    AvroDoctor.readInt(Array(0xff, 0xff, 0xff, 0xff, 0x0f).map(_.toByte), 0) shouldBe Some((Int.MinValue, 5))
  }

  test("readInt - round-trip via Avro encoder") {
    val values = List(0, 1, -1, 2, -2, 100, -100, 1000000, -1000000, Int.MaxValue, Int.MinValue)
    for (n <- values) {
      val bs = avroEncodeInt(n)
      AvroDoctor.readInt(bs, 0) shouldBe Some((n, bs.length))
    }
  }

  test("readInt - non-zero start index") {
    val bs = avroEncodeInt(42)
    val padded = 0xab.toByte +: bs
    AvroDoctor.readInt(padded, 1) shouldBe Some((42, 1 + bs.length))
  }

  test("readInt - empty / truncated returns None") {
    AvroDoctor.readInt(Array.empty, 0) shouldBe None
    // multi-byte value truncated after first continuation byte
    AvroDoctor.readInt(Array(0x80.toByte), 0) shouldBe None
  }

  test("readLong - spec examples") {
    AvroDoctor.readLong(Array(0x00.toByte), 0) shouldBe Some((0L, 1))
    AvroDoctor.readLong(Array(0x01.toByte), 0) shouldBe Some((-1L, 1))
    AvroDoctor.readLong(Array(0x02.toByte), 0) shouldBe Some((1L, 1))
    AvroDoctor.readLong(Array(0x03.toByte), 0) shouldBe Some((-2L, 1))
  }

  test("readLong - round-trip via Avro encoder") {
    val values = List(0L, 1L, -1L, 100L, -100L, Int.MaxValue.toLong, Int.MinValue.toLong, Long.MaxValue, Long.MinValue, 1000000000000L, -1000000000000L)
    for (n <- values) {
      val bs = avroEncodeLong(n)
      AvroDoctor.readLong(bs, 0) shouldBe Some((n, bs.length))
    }
  }

  test("readLong - empty / truncated returns None") {
    AvroDoctor.readLong(Array.empty, 0) shouldBe None
    AvroDoctor.readLong(Array(0x80.toByte), 0) shouldBe None
  }

  test("readBoolean - round-trip via Avro encoder") {
    List(true, false).foreach { b =>
      val bs = avroEncodeBoolean(b)
      bs.length shouldBe 1
      AvroDoctor.readBoolean(bs, 0) shouldBe Some((b, 1))
    }
  }

  test("readBoolean - empty returns None") {
    AvroDoctor.readBoolean(Array.empty, 0) shouldBe None
  }

  test("readFloat - round-trip via Avro encoder") {
    val values = List(0f, 1f, -1f, 0.5f, -0.5f, Float.MaxValue, Float.MinPositiveValue, Float.NaN)
    for (n <- values) {
      val bs = avroEncodeFloat(n)
      bs.length shouldBe 4
      val Some((decoded, idx)) = AvroDoctor.readFloat(bs, 0)
      if (n.isNaN) decoded.isNaN shouldBe true
      else decoded shouldBe n
      idx shouldBe 4
    }
  }

  test("readFloat - truncated returns None") {
    AvroDoctor.readFloat(Array.empty, 0) shouldBe None
    AvroDoctor.readFloat(Array(0x00, 0x00, 0x00).map(_.toByte), 0) shouldBe None
  }

  test("readDouble - round-trip via Avro encoder") {
    val values = List(0.0, 1.0, -1.0, 0.5, -0.5, Double.MaxValue, Double.MinPositiveValue, Double.NaN)
    for (n <- values) {
      val bs = avroEncodeDouble(n)
      bs.length shouldBe 8
      val Some((decoded, idx)) = AvroDoctor.readDouble(bs, 0)
      if (n.isNaN) decoded.isNaN shouldBe true
      else decoded shouldBe n
      idx shouldBe 8
    }
  }

  test("readDouble - truncated returns None") {
    AvroDoctor.readDouble(Array.empty, 0) shouldBe None
    AvroDoctor.readDouble(Array.fill(7)(0x00.toByte), 0) shouldBe None
  }

  test("readString - round-trip via Avro encoder") {
    val values = List("", "hello", "世界", "a" * 1000, "emoji: 🎉")
    for (s <- values) {
      val bs = avroEncodeString(s)
      AvroDoctor.readString(bs, 0) shouldBe Some((s, bs.length))
    }
  }

  test("readString - empty array returns None") {
    AvroDoctor.readString(Array.empty, 0) shouldBe None
  }

  test("readString - truncated data returns None") {
    val bs = avroEncodeString("hello")
    AvroDoctor.readString(bs.init, 0) shouldBe None
  }

  test("guess - finds schema-matching solution for ABC(Int, String, Long, String)") {
    import AvroValue._
    import org.apache.avro.generic.GenericDatumWriter

    val abc = ABC(42, "Аhello", 100L, "world")

    val bs: Array[Byte] = {
      val Right(schema) = ABC.codec.schema
      val Right(record) = ABC.codec.encode(abc)
      val bos = new java.io.ByteArrayOutputStream()
      val enc = org.apache.avro.io.EncoderFactory.get().binaryEncoder(bos, null)
      new GenericDatumWriter[Any](schema).write(record, enc)
      enc.flush()
      bos.toByteArray
    }

    val solutions = AvroDoctor.guess(bs)

    solutions should contain(List(
      AvroInt(42),
      AvroString("Аhello"),
      AvroLong(100L),
      AvroString("world"),
    ))

    solutions.foreach(sol => info(s"solution: $sol"))
    pprint.log(solutions.size)
  }

}
