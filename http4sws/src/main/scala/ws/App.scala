package ws

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Ref
import cats.effect.std.Queue
import fs2.Stream
import fs2.concurrent.Topic
import scala.concurrent.duration.*
import ws.Server.server
import ws.core.ChatState
import ws.core.InputMessage
import ws.core.KeepAlive
import ws.core.OutputMsg
import ws.core.Protocol

object App extends IOApp.Simple {

  val program = for {
    q        <- Queue.unbounded[IO, OutputMsg]
    t        <- Topic[IO, OutputMsg]
    state    <- Ref.of[IO, ChatState](ChatState.fresh)
    protocol <- IO(Protocol.make[IO](state))
    im       <- IO(InputMessage.make[IO](protocol))
    s1 = Stream.fromQueueUnterminated(q).through(t.publish)                 // all came into `t` forwarded to `t`
    s2 = Stream.awakeEvery[IO](10.seconds).as(KeepAlive).through(t.publish) // ping/pong
    s3 = Stream.eval(server[IO](q, t, im, protocol, state))                 // run http server forever
    s        <- Stream(s1, s2, s3).parJoinUnbounded.compile.drain
  } yield s

  override def run: IO[Unit] = program

}
