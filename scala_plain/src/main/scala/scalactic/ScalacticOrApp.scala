package scalactic

import org.scalactic.{Bad, Good, Or}

import scala.util.Try

object ScalacticOrApp extends App {

  type R = Or[Int, String]

  def makeInt(origin: String): Int Or String = try {
    Good(origin.toInt)
  } catch {
    case e: NumberFormatException => Bad(e.getMessage)
  }

  println(makeInt("5"))
  println(makeInt("5x"))

  val swapped: Either[Int, Throwable] = Try("5".toInt).toEither.swap

}
