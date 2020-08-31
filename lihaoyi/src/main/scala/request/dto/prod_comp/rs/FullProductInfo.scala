package request.dto.prod_comp.rs

case class FullProductInfo(
  active: Boolean,
  name: String,
  pin: Boolean,
  scientifiRrating: Integer,
  starRating: Integer,
  productImageUrl: String,
  brand: Brand,
  price: MonetaryAmount,
  unitType: List[String],
  metaData: MetaData,
  seller: Seller,
)
