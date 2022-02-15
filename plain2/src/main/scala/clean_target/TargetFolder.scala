package clean_target

import java.io.File

object TargetFolder {

  def deleteDirWithFiles(file: File): Unit = {
    Option(file.listFiles).foreach(_.foreach(deleteDirWithFiles))
    file.delete
  }

  def remove(file: File): Unit = {
    val chunks = file.toPath.toString.split("/")
    val toDelete = chunks.take(chunks.length - 1) :+ "target"
    val toDeleteF = new File(toDelete.mkString("/", "/", ""))

    if (toDeleteF.exists && toDeleteF.isDirectory) {
      deleteDirWithFiles(toDeleteF)
      println(s"${file.toString} - REMOVING TARGET")
    }
  }

}
