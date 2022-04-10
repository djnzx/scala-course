package crawler

import java.io.File

object AppCleanTargetFolder extends CrawlerApp {
  val ~ = "/Users/alexr/"
  override val rootFolder = s"${~}dev"
  override val suffixesToDetect: Set[String] = Set("pom.xml", "build.sbt", "gradlew.bat", "/src", "/project")

  override def callbackPreAction(file: File, level: Int): Unit = TargetOps.removeRecursively(file)
}
