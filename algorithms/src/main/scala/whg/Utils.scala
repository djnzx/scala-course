package whg

object Utils {
  def wrong(msg: String) = throw new IllegalArgumentException(msg)
  def obtainResource(fileName: String) = 
    Option(getClass.getClassLoader.getResource(fileName)).map(_.getFile)
}
