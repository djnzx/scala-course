package json

import scala.io.Source

object Utils {

  def wrongState(message: String) = throw new IllegalArgumentException(message)

  private def readContents(fileName: String): String = {
    val src = Source.fromFile(fileName)
    val content = src.getLines().mkString("\n")
    src.close()
    content
  }

  private def readResourceContents(resourceName: String) =
    readContents(getClass.getClassLoader.getResource(resourceName).getFile)

  def readJsonConfig(resourceName: String) = {
    val contents = readResourceContents(resourceName)
    val json = io.circe.parser.decode[MappingsConfig](contents)
    json.getOrElse(wrongState("Config should be readable JSON"))
  }

  def readDataAsJson(resourceName: String) = {
    val contents = readResourceContents(resourceName)
    val json = io.circe.parser.parse(contents)
    json.getOrElse(wrongState("Config should be readable JSON"))
  }

}
