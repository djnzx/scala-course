package ws

import cats.MonadThrow
import cats.effect.kernel.Ref
import cats.implicits.*
import fs2.io.file
import fs2.io.file.Files
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.MediaType
import org.http4s.Request
import org.http4s.Response
import org.http4s.StaticFile
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.server.websocket.WebSocketBuilder2
import ws.core.ChatState

class Routes[F[_]: Files: MonadThrow] extends Http4sDsl[F] {

  private val htmlFile = fs2.io.file.Path(getClass.getClassLoader.getResource("chat.html").getFile)

  def endpoints(
      state: Ref[F, ChatState], // only to expose metrics
      mkWsHandler: WebSocketBuilder2[F] => F[Response[F]]
    ): WebSocketBuilder2[F] => HttpApp[F] =
    wsb =>
      HttpRoutes
        .of[F] {

          /** download chat.html (including js) */
          case rq @ GET -> Root / "chat" =>
            StaticFile
              .fromPath(htmlFile, Some(rq))
              .getOrElseF(NotFound()) // 404 if file not found

          /** provide metrics */
          case GET -> Root / "metrics"   =>
            state.get
              .map(_.metricsAsHtml)
              .flatMap(Ok(_, `Content-Type`(MediaType.text.html)))

          /** handle WS traffic */
          case GET -> Root / "ws"        => mkWsHandler(wsb)
        }
        .orNotFound // 404 if other URL requested

}
