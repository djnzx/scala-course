package request.dto.optimisation.rs

import upickle.default.{macroRW, ReadWriter => RW}

case class ShoppingListProduct(
  id: Long,
  productId: Long,
  sellerId: Long,
  quantity: Int,
  pinned: Boolean,
  crossedOff: Boolean,
  metadata: ListItemMetadata
)

object ShoppingListProduct {
  implicit val rw: RW[ShoppingListProduct] = macroRW
}
