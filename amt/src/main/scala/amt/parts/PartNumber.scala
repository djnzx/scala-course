package amt.parts

sealed trait PartNumber {
  def oemNumber: String
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

final case class PartNumberNotFound(oemNumber: String) extends PartNumber
final case class PartNumberFound(
    oemNumber: String,
    oemOriginal: String,
    replace: Option[String],
    description: String,
    descriptionR: Option[String],
    brand: String,
    supplier: String,
    weight: Option[Float],
    priceAvia: Float,
    priceCntr: Float,
    priceCore: Option[Float])
    extends PartNumber

object PartNumberFound {

  def apply(raw: java.util.Map[String, Object]): PartNumberFound = PartNumberFound(
    raw.get("oem").asInstanceOf[String],
    raw.get("oem_original").asInstanceOf[String],
    Option(raw.get("replace").asInstanceOf[String]).filter(_.nonEmpty),
    raw.get("descr").asInstanceOf[String],
    Option(raw.get("descr_ru").asInstanceOf[String]).filter(_.nonEmpty),
    raw.get("brand").asInstanceOf[String],
    raw.get("supplier").asInstanceOf[String],
    raw.get("weight").asInstanceOf[Any] match {
      case i: Int   => Some(i.toFloat).filter(_ > 0)
      case f: Float => Some(f).filter(_ > 0)
      case _        => None
    },
    raw.get("list_price").asInstanceOf[Float],
    raw.get("list_price_cntr").asInstanceOf[Float],
    Option(raw.get("core_price").asInstanceOf[Float]).filter(_ > 0),
  )

}
