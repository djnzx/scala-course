package count_java

import java.io.File

object CleanTargetApp extends App {

  val filesToDetect = Map(
    "pom.xml"     -> "target",
    ".sbt"        -> "target",
    "gradlew.bat" -> ".gradle",
  )
  val rootFolder = new File("~/dev")

  def filter(f: File) = f.isFile && filesToDetect.exists { case (suffix, _) => f.toString.toLowerCase.endsWith(suffix) }

}
