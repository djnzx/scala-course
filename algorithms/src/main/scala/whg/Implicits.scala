package whg

import scala.language.implicitConversions

/**
  * implicit convertors, are used only in tests 
  */
object Implicits {
  implicit def toLoc(s: String) = Loc.parseOrDie(s)
  implicit def toMove(s: String) = Move.parseOrDie(s)
}
