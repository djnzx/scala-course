package skunkx

import cats.effect.IO
import cats.effect.Resource
import skunk.Session
import natchez.Trace.Implicits.noop

trait SkunkConnection {

  val session: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 5432,
      database = "world",
      user = "jimmy",
      password = Some("banana")
    )

}
