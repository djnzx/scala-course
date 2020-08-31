package request.dto.prod_comp.rs

import java.util.Date

import request.dto.optimisation.rs.{ShoppingListCategory, ShoppingListProduct}

case class ShoppingList(
  id: Long,
  imageName: String,
  description: String,
  shoppingListDate: Date,
  shoppingListUpdatedDate: Date,
  customerId: String,
  shoppingListCustomName: String,
  shoppingListIsActive: Boolean,
  listOrder: List[Integer],
  shoppingListProducts: List[ShoppingListProduct],
  shoppingListCategories: List[ShoppingListCategory],
)
