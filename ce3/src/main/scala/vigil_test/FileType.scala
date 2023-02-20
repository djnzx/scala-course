package vigil_test

sealed abstract class FileType(val ext: String, val lineParser: LineParser) {
  def nameWoExt(name: String): String = name.substring(0, name.length - ext.length)
}

object FileType {

  case object CSV extends FileType(".CSV", LineParser.CSVLineParser)
  case object TSV extends FileType(".TSV", LineParser.TSVLineParser)

  import cats.implicits.catsSyntaxOptionId
  def detect(fileName: String): Option[FileType] = fileName.toUpperCase match {
    case x if x.endsWith(CSV.ext) => FileType.CSV.some
    case x if x.endsWith(TSV.ext) => FileType.TSV.some
    case _                        => None
  }

  def detectUnsafe(fileName: String): FileType = detect(fileName).getOrElse(sys.error(s"Unsupported file type given: $fileName"))

}

object FileTypeTest extends App {
  import FileType._
  import pprint._

  pprintln(detectUnsafe("1.CSV"))
  pprintln(detectUnsafe("2.TSV"))
  pprintln(detect("3.TXT"))
}
