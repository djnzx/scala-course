package intro0

object ScalaVersion {

  private val props = new java.util.Properties
  props.load(getClass.getResourceAsStream("/library.properties"))
  val line: String = props.getProperty("version.number")

}
