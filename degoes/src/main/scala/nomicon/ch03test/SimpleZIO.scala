package nomicon.ch03test

object SimpleZIO extends zio.App {
  
  import zio._
  import zio.console._

  val greet_app: ZIO[Console, Nothing, Unit] = for {
    name <- getStrLn.orDie
      _  <- putStrLn(s"Hello, $name!")
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = greet_app.exitCode
}

import zio.test.DefaultRunnableSpec
import zio.test.environment._
import zio.test.Assertion._
import zio.test._

object ExampleSpec extends DefaultRunnableSpec {
  def spec = suite("ExampleSpec")(
    testM("greet says hello to the user") {
      for {
        _     <- TestConsole.feedLines("Jane")
        _     <- SimpleZIO.greet_app
        value <- TestConsole.output
      } yield assert(value)(equalTo(Vector("Hello, Jane!\n")))
    }
  )
}

