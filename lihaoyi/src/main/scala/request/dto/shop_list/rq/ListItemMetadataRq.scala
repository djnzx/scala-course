package request.dto.shop_list.rq

import upickle.default.{macroRW, ReadWriter => RW}

import request.dto.optimisation.rs.ListItemMetadata.State
import request.dto.optimisation.rs.Weight

case class ListItemMetadataRq(
  state: State,
  weight: Weight,
  brand: String
)

object ListItemMetadataRq {
  implicit val rw: RW[ListItemMetadataRq] = macroRW
}