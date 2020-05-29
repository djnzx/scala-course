package fps

import org.scalactic.{Bad, Good, Or}

import scala.util.Try

object Fps014ErrorHandling extends App {

  def makeInt(origin: String): Int Or String = try {
    Good(origin.toInt)
  } catch {
    case e: NumberFormatException => Bad(e.getMessage)
  }

  println(makeInt("5"))
  println(makeInt("5x"))

  val swapped: Either[Int, Throwable] = Try("5".toInt).toEither.swap

}
