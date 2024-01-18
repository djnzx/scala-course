package hackerrankfp.d200421

import scala.io.BufferedSource

object IOSourceApp extends App {
  val src: BufferedSource = scala.io.Source.stdin
  val r = src.getLines().take(2).map(_.toInt).sum
  println(r)
}
