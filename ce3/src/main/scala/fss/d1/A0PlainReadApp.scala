package fss.d1

import A0Logic.evalFileName
import java.io.BufferedReader
import java.io.FileReader
import scala.util.Using

object A0PlainReadApp extends App {

  val f = evalFileName("data_c.txt")
  println(f)

  Using(new BufferedReader(new FileReader(f))) { r =>
    r.lines()
      .forEach { s =>
        s.toIntOption.foreach(c => println(s"C: $c, F: ${A0Logic.cToF(c.toDouble)}"))
      }
  }

}
