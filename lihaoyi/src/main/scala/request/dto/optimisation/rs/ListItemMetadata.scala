package request.dto.optimisation.rs

import upickle.default.{macroRW, ReadWriter => RW}

case class ListItemMetadata(
  id: Long, 
  state: ListItemMetadata.State,
  weight: Weight, 
  brand: String,
)

object ListItemMetadata {
  type State = String // SOLID, COMPLEX, LIQUID, POWDER, GAS
  implicit val rw: RW[ListItemMetadata] = macroRW
}
