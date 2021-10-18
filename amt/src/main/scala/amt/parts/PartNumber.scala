package amt.parts

sealed trait PartNumber {
  def oem: String
}

object PartNumber {
  private val VAL_NOT_FOUND = "No Part Found";
  private val FIELD_DESCR = "descr";
  private val FIELD_OEM = "oem";

  def parse(raw: java.util.Map[String, Object]): PartNumber =
    if (raw.get(FIELD_DESCR).asInstanceOf[String] == VAL_NOT_FOUND)
      PartNumberNotFound(raw.get(FIELD_OEM).asInstanceOf[String])
    else PartNumberFound(raw)

}

final case class PartNumberNotFound(oem: String) extends PartNumber
final case class PartNumberFound(
    oem: String,
    oemOriginal: String,
    replace: String,
    description: String,
    descriptionR: String,
    brand: String,
    supplier: String,
    weight: Float,
    priceAvia: Float,
    priceCntr: Float,
    priceCore: Float)
    extends PartNumber

object PartNumberFound {

  def apply(raw: java.util.Map[String, Object]): PartNumberFound = PartNumberFound(
    raw.get("oem").asInstanceOf[String],
    raw.get("oem_original").asInstanceOf[String],
    raw.get("replace").asInstanceOf[String],
    raw.get("descr").asInstanceOf[String],
    raw.get("descr_ru").asInstanceOf[String],
    raw.get("brand").asInstanceOf[String],
    raw.get("supplier").asInstanceOf[String],
    raw.get("weight").asInstanceOf[Any] match {
      case i: Int   => i.toFloat
      case f: Float => f
      case _        => 0
    },
    raw.get("list_price").asInstanceOf[Float],
    raw.get("list_price_cntr").asInstanceOf[Float],
    raw.get("core_price").asInstanceOf[Float],
  )

}
