package request.dto.optimisation.rq

import upickle.default.{macroRW, ReadWriter => RW}

case class ACTFullCatalogInfoRq(
  latitude: Double,
  longitude: Double,
  customerId: String,
  city: String,
  town: String,
  country: String,
  sellerName: String,
  locationType: ACTFullCatalogInfoRq.LocationType,
  shoppingListId: Long,
  co2Weight: Double, // 0..1 (0-100%)
  priceWeight: Double, // 0..1
)

object ACTFullCatalogInfoRq {
  type LocationType = String
  implicit val rw: RW[ACTFullCatalogInfoRq] = macroRW
}
