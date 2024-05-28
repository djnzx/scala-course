package app

import java.net.InetSocketAddress
import sttp.tapir
import sttp.tapir.Endpoint
import sttp.tapir.EndpointInput
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.ztapir._
import zio._
import zio.http.Server

/** https://zio.dev/guides/
  */
object ZioHttpLauncher extends ZIOAppDefault {

  /** pathSegment parse */
  def configIdPathSegment: EndpointInput.PathCapture[String] =
    tapir
      .path[String]("item")

  /** endpoint definition */
  val test123: Endpoint[Unit, String, Unit, RuntimeFlags, Any] =
    endpoint
      //      .in(stringBody)
      //      .out(plainBody[Int])
      .get
      .in("test")
      .in(configIdPathSegment)
      .out(plainBody[Int])

  /** wiring implementation */
  val test123impl = test123
    .zServerLogic(mkMsg)

  def mkMsg(s: String) = ZIO.succeed(s.length)

  override def run = {

    val httpEndpoints = ZioHttpInterpreter()
      .toHttp(test123impl)

    val cfgLayer: ZLayer[Any, Nothing, Server.Config] = ZLayer {
      ZIO.succeed(Server.Config.default.binding(new InetSocketAddress(8080)))
    }

    Server.serve(httpEndpoints)
      .provide(
        Server.live,
        cfgLayer
      )
  }

}
