package request.dto.optimisation.rs

import upickle.default.{macroRW, ReadWriter => RW}

case class ShoppingListCategory(
  id: Long,
  categoryId: Long,
  quantity: Int,
  pinned: Boolean,
  metadata: ListItemMetadata,
)

object ShoppingListCategory {
  implicit val rw: RW[ShoppingListCategory] = macroRW
}