package request.dto.optimisation.rs

import upickle.default.{macroRW, ReadWriter => RW}

case class Weight(
  id: Long,
  unit: String,
  value: Double
)

object Weight {
  implicit val rw: RW[Weight] = macroRW
}