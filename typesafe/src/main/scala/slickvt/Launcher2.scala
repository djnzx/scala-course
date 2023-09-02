package slickvt

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.implicits._
import slick.jdbc.PostgresProfile.api.{Tag => _, _}
import slickvt.model._

object Launcher2 extends App with Impl {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()

  val rq = RQuery(brandId = "bccashgames",
    tagFilter = TagFilter(
      includeTags = Seq(
        PTag("TAG-completed",0),
        PTag("TAG-promo_test",0),
        PTag("TAG-testcampaign",0),
      ),
      excludeTags = Seq(),
    ).some,
//    playerId = "PLAYER-fea4a1cf-c83d-40dc-803a-60f46a813390".some
  )

  findProfilesWithTagsSource(rq)
    .filter{case(_,ts)=>ts.size > 1}
//    .take(1)
    .runForeach(x => pprint.pprintln(x))

}
