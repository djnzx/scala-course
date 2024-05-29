package app

import java.net.InetSocketAddress
import java.util.UUID
import scala.util.Try
import sttp.tapir
import sttp.tapir.DecodeResult
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.ztapir._
import zio._
import zio.http.Server

/** https://zio.dev/guides/
  */
object ZioHttpLauncher extends ZIOAppDefault {

  case class ConfigId(uuid: UUID) {
    def show = ConfigId.prefix.concat(uuid.toString)
  }
  object ConfigId {
    private val prefix = "CID-"
    def parse(s: String): Option[ConfigId] =
      Option(s)
        .filter(_.startsWith(prefix))
        .map(_.stripPrefix(prefix))
        .flatMap(s => Try(UUID.fromString(s)).toOption)
        .map { x => pprint.log(x); x }
        .map(ConfigId.apply)
  }

  def configIdPathSegment =
    tapir
      .path[String]("item")
      .mapDecode { s =>
        ConfigId.parse(s) match {
          case Some(value) => DecodeResult.Value(value)
          case None        => DecodeResult.Error(s, new RuntimeException)
        }
      }(_.show)

  val test123def =
    endpoint
      .get
      .in("test")
      .in(configIdPathSegment)
      .out(plainBody[String])

  def myLogic(x: ConfigId) = ZIO.succeed(x.uuid.toString.toLowerCase)

  val test123impl = test123def
    .zServerLogic(myLogic)

  override def run = {

    val httpEndpoints = ZioHttpInterpreter()
      .toHttp(test123impl)

    val webServerConfig: ZLayer[Any, Nothing, Server.Config] = ZLayer {
      ZIO.succeed(Server.Config.default.binding(new InetSocketAddress(8080)))
    }

    Server
      .serve(httpEndpoints)
      .provide(Server.live, webServerConfig)
  }

}
