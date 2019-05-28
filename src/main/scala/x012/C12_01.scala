package x012

import scala.io.Source
import scala.sys.process._

object C12_01 extends App {
  val source = Source.fromFile("README.md")
  for (line <- source) {
    println(line)
  }
  source.getLines().toList
  source.getLines().toArray
  source.close()

  val r = "ls -la" !!

  print(r)
}
