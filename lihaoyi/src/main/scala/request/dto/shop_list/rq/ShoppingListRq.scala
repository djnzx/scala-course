package request.dto.shop_list.rq

import upickle.default.{macroRW, ReadWriter => RW}

import java.util.Date

case class ShoppingListRq(
  customerId: String,
  imageName: String,
  description: String,
  shoppingListCustomName: String,
  shoppingListIsActive: Boolean,
  shoppingListUpdatedDate: String, //Date,
  shoppingListDate: String, //Date,
  listOrder: List[Int],
  shoppingListProducts: List[ShoppingListProductRq],
  shoppingListCategories: List[ShoppingListCategoryRq]
)

object ShoppingListRq {
  implicit val rw: RW[ShoppingListRq] = macroRW
}
