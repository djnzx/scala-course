package crawler

import java.io.File

object AppCleanIdeaAndGitFolders extends CrawlerApp {
  val ~ = "/Users/alexr/"
  override val rootFolder = s"${~}dev/abb/steps"
  override val suffixesToDetect: Set[String] = Set(".git", ".idea", ".iml", ".gitignore")

  override def callbackPreAction(file: File, level: Int): Unit = TargetOps.rmRF(file)
}
