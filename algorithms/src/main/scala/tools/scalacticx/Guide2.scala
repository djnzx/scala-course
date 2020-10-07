package tools.scalacticx

import org.scalactic._
import TripleEquals._
import Tolerance._

object Guide2 {
  val outcome = 2.000001
  val rough   = outcome === 2.0 +- .001     // true
  val precise = outcome === 2.0 +- .0000001 // false
}
