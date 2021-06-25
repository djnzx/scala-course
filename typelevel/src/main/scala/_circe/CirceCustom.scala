package _circe

import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
import io.circe.{Decoder, Encoder, HCursor}

final case class InventoryWorkflow(
                                  id: Int,
                                  name: String,
                                  extra: Option[Int],
                                  details: Option[String]
                                  )
object InventoryWorkflow {
  implicit val encoder: Encoder[InventoryWorkflow] = deriveEncoder
  implicit val decoder: Decoder[InventoryWorkflow] = (c: HCursor) =>
    for {
      id <- c.downField("id").as[Int]
      name <- c.downField("name").as[String]
      extra <- c.downField("extra").as[Option[Int]]
      details <- c.downField("details").as[Option[String]]
    } yield
      new InventoryWorkflow(id, name, extra, details)
}

object CirceCustom extends App {
  val iwf1 = InventoryWorkflow(1, "Alex", Some(33), Some("extra"))
  val iwf2 = InventoryWorkflow(1, "Alex", Some(33), None)
  println(iwf2.asJson.noSpaces)

  val raw1 = """{"id":1,"name":"Alex","extra":33, "details":"x"}""".stripMargin
  val raw2 = """{"id":1,"name":"Alex","extra":33}""".stripMargin
  val raw3 = """{"id":1,"name":"Alex"}""".stripMargin
  println(io.circe.parser.decode[InventoryWorkflow](raw1))
  println(io.circe.parser.decode[InventoryWorkflow](raw2))
  println(io.circe.parser.decode[InventoryWorkflow](raw3))
}
