package request.dto.optimisation.rs

case class OptimisedList(
  shoppingListCategories: List[ShoppingListCategory],
  shoppingListProducts: List[ShoppingListProduct]
)
