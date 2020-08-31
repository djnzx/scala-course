package request.dto.shop_list.rq

import upickle.default.{macroRW, ReadWriter => RW}

case class ShoppingListProductRq(
  productId: Long,
  sellerId: Long,
  quantity: Int,
  pinned: Boolean,
  crossedOff: Boolean,
  metadata: ListItemMetadataRq
)

object ShoppingListProductRq {
  implicit val rw: RW[ShoppingListProductRq] = macroRW
}