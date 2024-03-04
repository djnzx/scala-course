package ws

import cats.MonadThrow
import cats.effect.kernel.Ref
import cats.implicits._
import fs2.io.file.Files
import org.http4s.HttpApp
import org.http4s.HttpRoutes
import org.http4s.MediaType
import org.http4s.Response
import org.http4s.StaticFile
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.server.websocket.WebSocketBuilder2
import ws.core.ChatState

class Routes[F[_]: Files: MonadThrow] extends Http4sDsl[F] {

  private val htmlFile = fs2.io.file.Path(getClass.getClassLoader.getResource("chat.html").getFile)

  private def index =
    HttpRoutes.of[F] { case rq @ GET -> Root / "chat" =>
      StaticFile.fromPath(htmlFile, Some(rq)).getOrElseF(NotFound()) // 404 if file not found
    }

  private def metrics(stateRef: Ref[F, ChatState[F]]) =
    HttpRoutes.of[F] { case GET -> Root / "metrics" =>
      stateRef.get.map(_.metricsAsHtml).flatMap(Ok(_, `Content-Type`(MediaType.text.html)))
    }

  private def ws(rs: F[Response[F]]) =
    HttpRoutes.of[F] { case GET -> Root / "ws" => rs }

  def endpoints(
      stateRef: Ref[F, ChatState[F]], // TODO: replace it with protocol, in order not to leak this Ref[ChatState]
      mkWsHandler: WebSocketBuilder2[F] => F[Response[F]]
    ): WebSocketBuilder2[F] => HttpApp[F] =
    wsb => (index <+> metrics(stateRef) <+> ws(mkWsHandler(wsb))).orNotFound // 404 if another URL

}
