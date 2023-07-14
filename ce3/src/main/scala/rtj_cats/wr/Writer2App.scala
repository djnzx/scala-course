package rtj_cats.wr

import cats._
import cats.data._
import cats.implicits._

object Writer2App extends App {

  def tab(n: Int) = " " * (5 - n)

  def countAndSayPlain(n: Int): Unit = n match {
    case 0 =>
      val msg = tab(n) + "starting unwinding!"
      println(msg)
    case n =>
      val msgPre = tab(n) + s"winding $n"
      val msgPost = tab(n) + s"unwinding $n"

      println(msgPre)
      countAndSayPlain(n - 1)
      println(msgPost)
  }

  countAndSayPlain(5)

  /** lifters:
    * --------
    *   - apply
    *   - value
    *   - tell
    *
    * modifiers:
    * ----------
    *   - map
    *   - mapWritten
    *   - mapBoth
    *   - bimap
    *   - reset (log)
    */
  // describe
  def countAndSayFp(n: Int): Writer[Vector[String], Unit] = n match {
    case 0 =>
      // the same value
      val msg = tab(n) + "starting unwinding!"
      // constructing the PART of message
      Writer(Vector(msg), ())
    case n =>
      // the same values
      val msgPre = tab(n) + s"winding $n"
      val msgPost = tab(n) + s"unwinding $n"
      // "lifted"
      val msgPreLifted: Writer[Vector[String], Unit] = Vector(msgPre).tell
      val msgPostLifted: Writer[Vector[String], Unit] = Vector(msgPost).tell
      // combined "automatically"
      for {
        _ <- msgPreLifted
        _ <- countAndSayFp(n - 1)
        _ <- msgPostLifted
      } yield ()
  }

  //
  val (log, _) = countAndSayFp(5).run
  val s = log.mkString("\n")
  println(s)
}
