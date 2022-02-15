package clean_target

import java.io.File

class RecursiveFolderCrawler(filter: File => Boolean, processor: File => Unit) {

  private def scanr(file: File, level: Int): Unit = {
    if (filter(file)) processor(file)
    if (file.isDirectory) file.listFiles.foreach(f => scanr(f, level + 1))
  }

  def scan(file: File): Unit = scanr(file, 0)

}
