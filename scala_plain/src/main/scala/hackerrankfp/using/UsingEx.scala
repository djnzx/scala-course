package hackerrankfp.using

import java.io.File

object UsingEx extends App {

  import scala.io.Source.fromFile
  import scala.util.Using
  
  /**
    * Idea of loading file from  
    * resources folder in one line
    */
    
  def file(name: String): File = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)

  def unhandled[A](t: Throwable): A = ???
  
  def loadFileFromResourcesToString(fname: String, extra: String => String = identity, handler: Throwable => String = unhandled[String]): String =
    Using(fromFile(file(fname))) { 
      _.getLines()
        .map(extra)
        .toVector.mkString("\n")
    }.fold(handler(_), identity)

  implicit class FileReadingSyntax(s: String) {
    def toLine(extra: String => String = identity, handler: => Throwable => String = unhandled): String =
      loadFileFromResourcesToString(s, extra, handler)
  }

}
