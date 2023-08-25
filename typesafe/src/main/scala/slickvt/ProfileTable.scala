package slickvt

import slick.lifted.{TableQuery, Tag}
import slick.jdbc.PostgresProfile.api._
import slick.collection.heterogeneous.HNil
import java.time.LocalDateTime

case class Profile(brandId: String,
  playerUUID: String,
  country: Option[String],
  affiliateId: Option[String],
  registrationDate: LocalDateTime)


class ProfileTable(tag: Tag) extends Table[Profile](tag, "profile")  {

  def brandId = column[String]("brand_id")
  def playerUUID = column[String]("player_uuid")
  def country = column[Option[String]]("country")
  def affiliateId = column[Option[String]]("affiliate_id")
  def registrationDate = column[LocalDateTime]("registration_date")

  def pk = primaryKey("pk_profile", brandId -> playerUUID)

  override def * = (brandId :: playerUUID :: country :: affiliateId :: registrationDate :: HNil).mapTo[Profile]
}

object ProfileTable {
  final val profiles = TableQuery[ProfileTable]
}
