package crawler

import java.io.File

class RecursiveFolderCrawler(
    callbackPre: (File, Int) => Unit,
    callbackPost: (File, Int) => Unit) {

  private def scanr(file: File, level: Int): Unit = {
    callbackPre(file, level)
    if (file.isDirectory) file.listFiles.foreach(f => scanr(f, level + 1))
    callbackPost(file, level)
  }

  def scan(file: File): Unit = scanr(file, 0)

}
