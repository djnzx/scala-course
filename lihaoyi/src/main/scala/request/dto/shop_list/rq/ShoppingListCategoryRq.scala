package request.dto.shop_list.rq

import upickle.default.{macroRW, ReadWriter => RW}

case class ShoppingListCategoryRq(
  categoryId: Long,
  quantity: Int,
  pinned: Boolean,
  metadata: ListItemMetadataRq
)

object ShoppingListCategoryRq {
  implicit val rw: RW[ShoppingListCategoryRq] = macroRW
}
