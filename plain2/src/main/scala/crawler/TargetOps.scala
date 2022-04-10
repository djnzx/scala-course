package crawler

import java.io.File
import scala.sys.process.Process

object TargetOps {

  def deleteDirWithFiles(file: File): Unit = {
    Option(file.listFiles).foreach(_.foreach(deleteDirWithFiles))
    file.delete
  }

  def removeRecursively(file: File): Unit = {
    val chunks = file.toPath.toString.split("/")
    val toDelete = chunks.take(chunks.length - 1) :+ "target"
    val toDeleteF = new File(toDelete.mkString("/", "/", ""))

    if (toDeleteF.exists && toDeleteF.isDirectory) {
      deleteDirWithFiles(toDeleteF)
      println(s"${file.toString} - REMOVING TARGET")
    }
  }

  def rmRF(file: File): Unit = Process(s"rm -rf ${file.toString}") !!

}
