package crawler

import java.io.File

trait CrawlerApp {

  /** root folder to do the things */
  def rootFolder: String

  /** suffixes to detect */
  val suffixesToDetect: Set[String]

  protected def filter(f: File): Boolean = suffixesToDetect.exists { suffix => f.toString.toLowerCase.endsWith(suffix) }

  private def validateRootPath(root: String) = {
    val path = new File(root)
    if (!path.exists) sys.error("valid file or folder name is expected as a parameter to run the app")
    path
  }

  def callbackPreAction(file: File, level: Int): Unit

  /** callback to run before folder traversal */
  def callbackPre(file: File, level: Int): Unit = if (filter(file)) callbackPreAction(file, level) else ()

  /** callback to run after folder traversal */
  def callbackPost(file: File, level: Int): Unit = ()

  def main(args: Array[String]): Unit = {
    val root: File = validateRootPath(rootFolder)
    println(s"Root folder: $root")

    new RecursiveFolderCrawler(callbackPre, callbackPost).scan(root)
  }

}
