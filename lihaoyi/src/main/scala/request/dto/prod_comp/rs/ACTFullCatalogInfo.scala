package request.dto.prod_comp.rs

case class ACTFullCatalogInfo(
  id: Long,
  categoryName: String,
  active: Boolean,
  picture: String,
  pin: Boolean,
  metadata: MetaData,
  fullProductInfo: Set[FullProductInfo],
  parentId: Int
)
