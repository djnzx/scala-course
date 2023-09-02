package slickvt

import slick.jdbc.PostgresProfile.api.{Tag => _, _}

object Launcher extends App with SlickBase {

  val players = Seq(
    "PLAYER-8d299950-9a29-4bee-b53f-b544ad7f8d37",
    "PLAYER-6b6ff783-347d-4c42-a85b-aabda3774a99",
    "PLAYER-c04a1454-da67-4b27-8fb3-677564505d17"
  )

  /** select
    *       x2."brand_id", x2."player_uuid", x2."country", x2."affiliate_id", x2."registration_date",
    *       x3."tag_id",x3."brand_id", x3."player_uuid", x3."tag_id", x3."rank", x3."creation_date"
    * from
    *       "profile" x2
    * left outer join "tag" x3 on (x2."player_uuid" = x3."player_uuid")
    */
  // left outer join
  val q1 = // : Query[(ProfileTable, Rep[Option[TagTable]]), (Profile, Option[Tag]), Seq] =
    //                             ================              ===========
    ProfileTable.profiles
      .joinLeft(TagTable.tags)
      .on(_.playerUUID === _.playerUUID)
      .filter { case (p, _) => p.playerUUID inSet players }
      .map { case (p, t) => p.playerUUID -> t.map(_.tagId) }

  /** select
    *       x2."brand_id", x2."player_uuid", x2."country", x2."affiliate_id", x2."registration_date",
    *       3."brand_id", x3."player_uuid", x3."tag_id", x3."rank", x3."creation_date"
    * from "profile" x2,
    *       "tag" x3
    * where
    *       x2."player_uuid" = x3."player_uuid"
    */
  // join
  val q2: Query[(ProfileTable, TagTable), (Profile, Tag), Seq] = ProfileTable.profiles
    //                         =========            ===
    .join(TagTable.tags)
    .on(_.playerUUID === _.playerUUID)

  /** select
    *       x2."brand_id", x2."player_uuid", x2."country", x2."affiliate_id", x2."registration_date",
    *       x3."brand_id", x3."player_uuid", x3."tag_id", x3."rank", x3."creation_date" +
    * from
    *      "profile" x2,
    *      "tag" x3
    * where
    *       x2."player_uuid" = x3."player_uuid"
    */
  val q3: Query[(ProfileTable, TagTable), (Profile, Tag), Seq] = for {
    //                        ========             ===
    p <- ProfileTable.profiles
    t <- TagTable.tags
    if p.playerUUID === t.playerUUID
  } yield (p, t)

  q1.run
    .foreach(x => pprint.pprintln(x))

}
