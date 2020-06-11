package fp_red.red09

import scala.io.BufferedSource

object FileIO extends App {
  
  /**
    * obtain file from resources folder
    */
  def obtainFile(fname: String): String = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(fname).getFile)
    scala.util.Using(scala.io.Source.fromFile(file)) { src: BufferedSource =>
      src.getLines().map(_.trim).toVector.mkString("\n")
    }.fold(_ => ???, identity)
  }

  println(obtainFile("1.json"))

}
