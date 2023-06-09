package tools

import java.io.File
import java.nio.file.{Files, Paths}

object Utils {

  def absolutePathFromResources(name: String): String =
    getClass.getClassLoader.getResource(name).getFile

  def fileFromResources(name: String): File =
    new File(absolutePathFromResources(name))

  // JDK 11
  def contentsFromResources(name: String): String = ???
//    Files.readString(Paths.get(absolutePathFromResources(name)))

}
