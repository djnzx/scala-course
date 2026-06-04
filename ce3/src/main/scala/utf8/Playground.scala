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

  def mkColoredBin(s: String): String = {
    val prefix = s.take(s.indexWhere(_ != '1') + 1)
    val payload = s.substring(prefix.length)

    new StringBuilder(Console.RED)
      .append(prefix)
      .append(Console.RESET)
      .append(payload)
      .toString()
  }

  def mkColoredHex(s: String): String = Console.BLUE + s + Console.RESET

  def groupMultibyteUtf8(bytes: Array[Byte]): Array[Array[Byte]] =
    Iterator.unfold(0) { i =>
      Option.when(i < bytes.length) {
        val len = bytes(i) & 0xff match {
          case b if (b & 0b10000000) == 0b00000000 => 1
          case b if (b & 0b11100000) == 0b11000000 => 2
          case b if (b & 0b11110000) == 0b11100000 => 3
          case b if (b & 0b11111000) == 0b11110000 => 4
          case b                                   => throw new IllegalArgumentException(s"invalid UTF-8 lead byte: ${toBin(b.toByte)}")
        }
        bytes.slice(i, i + len) -> (i + len)
      }
    }.toArray

  def describeUtfContent(utf: String): Unit = {
    val bytes: Array[Byte] = utf.getBytes
    val grouped: Array[Array[Byte]] = groupMultibyteUtf8(bytes)

    def fmt[A](xs: Array[Array[A]]): String =
      xs.map(_.mkString("[", ", ", "]")).mkString("[", ", ", "]")

    val dec = grouped.map(_.map(b => b & 0xff))
    val hex = grouped.map(_.map(b => mkColoredHex("%02X".format(b))))
    val bin = grouped.map(_.map(toBin).map(mkColoredBin))

    printf("content:       `%s`\n", utf)
    printf("length:        %d\n", utf.chars().count())
    printf("bytes length:  %d\n", bytes.length)
    printf("bytes decimal: %s\n", fmt(dec))
    printf("bytes hex:     %s\n", fmt(hex))
    printf("bytes bin:     %s\n", fmt(bin))
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
