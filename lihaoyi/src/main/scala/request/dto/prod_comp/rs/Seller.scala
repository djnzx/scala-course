package request.dto.prod_comp.rs

case class Seller(
  id: Long,
  shopName: String,
  active: Boolean,
  picture: String,
  shopLocation: ShopLocation
)
