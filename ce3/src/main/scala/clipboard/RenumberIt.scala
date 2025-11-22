package clipboard

import scala.io.StdIn._
import scala.util.Using

object RenumberIt extends App {

  val xs = List.newBuilder[String]

  while (readLine("> ") != "q")
    Clipboard.get.foreach { s =>
      val s2 = Literature.remap(s)
      Clipboard.set(s2)
      println(s"$s => $s2")
      xs.addOne(s2)
    }

  val lines = xs.result()

  Using(new java.io.PrintWriter("lit-used.txt")) { w =>
    lines.foreach(w.println)
  }

}
