package request.dto.prod_comp.rs

case class FullCatalogInfo(
  actFullCatalogInfo: Set[ACTFullCatalogInfo],
  shoppingLists: ShoppingLists,
  sellers: List[Seller],
)
