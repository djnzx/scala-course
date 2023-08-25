package slickvt

import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import slick.collection.heterogeneous.HNil
import slick.lifted

import java.time.LocalDateTime

case class Tag(brandId: String,
  playerUUID: String,
  tagId: String,
  rank: Int,
  creationDate: LocalDateTime)

class TagTable(tag: lifted.Tag) extends Table[Tag](tag, "tag") {

  def brandId = column[String]("brand_id")
  def playerUUID = column[String]("player_uuid")
  def tagId = column[String]("tag_id")
  def rank = column[Int]("rank")
  def creationDate = column[LocalDateTime]("creation_date")

  def pk = primaryKey("pk_tag", (brandId, playerUUID, tagId))

  override def * = (brandId :: playerUUID :: tagId :: rank :: creationDate :: HNil).mapTo[Tag]
}

object TagTable {
  val tags = TableQuery[TagTable]
}
