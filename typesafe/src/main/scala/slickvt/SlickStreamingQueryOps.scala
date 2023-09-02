package slickvt

import akka.NotUsed
import akka.stream.scaladsl.Source
import scala.concurrent.Future
import slick.dbio.Effect
import slick.jdbc.ResultSetConcurrency
import slick.jdbc.ResultSetType
import slick.sql.FixedSqlStreamingAction

trait SlickStreamingQueryOps extends SlickBase {

  def runStream[A](sa: FixedSqlStreamingAction[Seq[A], A, Effect.Read]): Source[A, NotUsed] =
    Source.fromPublisher {
      db.stream(sa)
    }

}
