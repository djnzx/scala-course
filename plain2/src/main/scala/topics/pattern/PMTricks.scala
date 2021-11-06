package topics.pattern

import java.io.{BufferedReader, File, FileReader}

import scala.util.{Failure, Success, Try, Using}

object PMTricks extends App {
//  List(1,2,3) match {
//    case List(1, _*) :+ 3 => ???
//  }
//  List(1,2,2,2,2,3) match {
//    case List(1, _*) :+ 3 => ???
//  }

  import scala.io.Source
//  val scalaFileContents = Source.fromFile("a.txt").getLines()

  def open(path: String) = new File(path)

  implicit class RichFile(file: File) {
    def read() = Source.fromFile(file).getLines()
  }

  
  val readLikeABoss = open("a.txt").read().foreach(println)

  def loan[A <: AutoCloseable, B](resource: A)(block: A => B): B = {
    Try(block(resource)) match {
      case Success(result) =>
        resource.close()
        result
      case Failure(e) =>
        resource.close()
        throw e
    }
  }


}
