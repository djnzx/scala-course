package request.dto.prod_comp.rq

import upickle.default.{macroRW, ReadWriter => RW}

case class ACTFullCatalogInfoRq(
  langitude: Double, // latitude ?
  longitude: Double,
  city: String,
  town: String,
  country: String,
  sellerName: String,
  locationType: ACTFullCatalogInfoRq.LocationType, // enum: COORDINATES,TOWN,CITY,COUNTRY,SELLERNAME
  customerId: String,
)

object ACTFullCatalogInfoRq {
  type LocationType = String
  implicit val rw: RW[ACTFullCatalogInfoRq] = macroRW
}
