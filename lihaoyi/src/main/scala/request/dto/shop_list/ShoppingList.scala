package request.dto.shop_list

import java.util.Date

import request.dto.optimisation.rs.{ShoppingListCategory, ShoppingListProduct}
import upickle.default.{macroRW, ReadWriter => RW}

case class ShoppingList(
  id: Long,
  shoppingListDate: String,  //Date,
  shoppingListUpdatedDate: String,//Date,
  customerId: String,
  shoppingListCustomName: String,
  imageName: String,
  description: String,
  shoppingListIsActive: Boolean,
  shoppingListProducts: List[ShoppingListProduct],
  shoppingListCategories: List[ShoppingListCategory],
  listOrder: List[Int]
)

object ShoppingList {
  implicit val rw: RW[ShoppingList] = macroRW 
}