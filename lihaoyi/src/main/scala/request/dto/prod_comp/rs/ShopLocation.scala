package request.dto.prod_comp.rs

case class ShopLocation(
  locationID: Long,
  country: String,
  city: String,
  town: String,
  locationName: String,
  ipAddress: String,
  longitude: Long,
  latitude: Long,
)