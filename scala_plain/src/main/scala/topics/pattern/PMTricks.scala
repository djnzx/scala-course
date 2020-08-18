package topics.pattern

import java.io.{BufferedReader, File, FileReader}

import scala.util.Using

object PMTricks extends App {
  List(1,2,3) match {
    case List(1, _*) :+ 3 => ???
  }
  List(1,2,2,2,2,3) match {
    case List(1, _*) :+ 3 => ???
  }

  import scala.io.Source
  val scalaFileContents = Source.fromFile("1.txt").getLines()

  def open(path: String) = new File("1.txt")

  implicit class RichFile(file: File) {
    def read() = Source.fromFile(file).getLines()
  }

  val readLikeABoss = open("1.txt").read()

}
