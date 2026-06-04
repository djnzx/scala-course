package utf8

/** multibyte UTF8 playground */
object Playground extends App {

  def toBin(x: Byte): String = {
    val chars = Array.ofDim[Byte](8)
    (0 to 7)
      .foreach { bit =>
        chars(7 - bit) = ('0' + ((x >> bit) & 1)).toByte
      }
    new String(chars)
  }

  def mkColored(s: String): String = {
    val prefix = s.take(s.indexWhere(_ != '1') + 1)
    val payload = s.substring(prefix.length)

    new StringBuilder(Console.RED)
      .append(prefix)
      .append(Console.RESET)
      .append(payload)
      .toString()
  }

  def describeUtfContent(utf: String): Unit = {
    val bytes: Array[Byte] = utf.getBytes
    val per_char = bytes.length / utf.codePoints().count().toInt
    val dec: Array[Int] = bytes.map(b => b & 0xff)
    val hex: Array[String] = bytes.map(b => "%02X".format(b))
    val bin: Array[String] = bytes.map(toBin).map(mkColored)

    def groupGt1[A](xs: Iterable[A]) =
      per_char match {
        case 1 => xs.mkString("[", ", ", "]")
        case _ => xs.grouped(per_char).map(_.mkString("[", ", ", "]")).mkString("[", ", ", "]")
      }

    printf("content:       `%s`\n", utf)
    printf("length:        %d\n", utf.chars().count())
    printf("bytes length:  %d\n", bytes.length)
    printf("bytes per char:%d\n", per_char)
    printf("bytes decimal: %s\n", groupGt1(dec))
    printf("bytes hex:     %s\n", groupGt1(hex))
    printf("bytes bin:     %s\n", groupGt1(bin))
    println("-" * 50)
  }

  Seq(
    "hello",
    "Привет",
    "नमस्ते",
    "😀🤪😐🙄",
    "Ы",
    "aбन😐",
  ).foreach(describeUtfContent)

}
