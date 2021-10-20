package ce2.d07_resources

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import ce2.common.debug.DebugHelper

import scala.io.Source

case class Config(connectURL: String)

object Config {

  def fromSource(source: Source): IO[Config] =
    for {
      config <- IO(Config(source.getLines().next()))
      _ <- IO(s"read $config").debug
    } yield config

}

trait DbConnection {
  def query(sql: String): IO[String]
}

object DbConnection {

  def make(connectURL: String): Resource[IO, DbConnection] =
    Resource.make(
      IO(s"> opening Connection to $connectURL").debug *> IO(
        new DbConnection {
          def query(sql: String): IO[String] = IO(s"""(results for SQL "$sql")""")
        },
      ),
    )(_ => IO(s"< closing Connection to $connectURL").debug.void)
}

object A9_EarlyRelease extends IOApp {

  lazy val sourceResource: Resource[IO, Source] =
    Resource.make(
      IO(s"> opening Source to config").debug *> IO(Source.fromString(config)),
    )(source => IO(s"< closing Source to config").debug *> IO(source.close))

  /** natural order, keep nesting */
  lazy val configResource0: Resource[IO, Config] = // <1>
    for {
      source <- sourceResource
      config <- Resource.liftF(Config.fromSource(source)) // <2>
    } yield config

  /** fast order. we evaluate, mean close resource immediately */
  lazy val configResource: Resource[IO, Config] =
    Resource.eval(sourceResource.use(Config.fromSource))

  val dbConnectionResource: Resource[IO, DbConnection] =
    for {
      config <- configResource
      conn <- DbConnection.make(config.connectURL)
    } yield conn

  val query: IO[String] = dbConnectionResource
    .use { conn =>
      conn.query("SELECT * FROM users WHERE id = 12").debug
    }

  val app: IO[String] = query

  def run(args: List[String]): IO[ExitCode] =
    query
      .as(ExitCode.Success)

  val config = "exampleConnectURL"
}
