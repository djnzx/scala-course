package clean_target

import java.io.File

class RecursiveFolderCrawler(callback: File => Unit) {

  private def scanr(file: File, level: Int): Unit = {
    callback(file)
    if (file.isDirectory) file.listFiles.foreach(f => scanr(f, level + 1))
  }

  def scan(file: File): Unit = scanr(file, 0)

}
