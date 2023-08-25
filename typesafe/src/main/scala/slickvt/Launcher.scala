package slickvt

import slick.jdbc.PostgresProfile.api._

object Launcher extends App with SlickBase {

  val players = Seq(
    "PLAYER-8d299950-9a29-4bee-b53f-b544ad7f8d37",
    "PLAYER-6b6ff783-347d-4c42-a85b-aabda3774a99"
  )


  val q = for {
    p <- ProfileTable.profiles
    if p.playerUUID inSet players
    t <- TagTable.tags
    if p.playerUUID === t.playerUUID
  } yield (p.brandId, p.playerUUID, p.country, p.affiliateId, t.tagId)

  q.run
    .foreach(x => pprint.pprintln(x))

  pprint.pprintln(
    (("bccashgames", "PLAYER-8d299950-9a29-4bee-b53f-b544ad7f8d37", Some(value = "ZZ"), None, Seq("T1", "T2", "T3")))

  )

}
