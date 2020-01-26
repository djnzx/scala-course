package aa_cookbook.x004

import scala.xml.XML

class Foo {

  val text = {
    if (System.currentTimeMillis() % 2 == 0)
      "even" else "odd"
  }

  // lazy means it won't be calculated until accessed
  lazy val file = {
    println("actually reading the file")
    var lines = ""
    try {
      lines = scala.io.Source.fromFile("/etc/passwd").getLines().mkString("\n")
    } catch {
      case e: Exception => lines = "smth went wrong"
    }
    lines
  }

  lazy val xml = XML.load("")

  println(text)
}


