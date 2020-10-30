package gettingstarted.testing

import zio.ExitCode.success
import zio._
import zio.console._
import zio.test.Assertion._
import zio.test._
import zio.test.environment._

/**
  * really testable !!!
  * code:
  */
object GS11Code {
  def sayHello: URIO[Console, Unit] =
    console.putStrLn("Hello, World!")
}

/** application */
object GS11App extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    GS11Code.sayHello.as(success)
}

/** spec */
object GS11Spec extends DefaultRunnableSpec {
  def spec: ZSpec[TestEnvironment, Any] = suite("HelloWorldSpec")(
    testM("sayHello correctly displays output") {
      for {
        _      <- GS11Code.sayHello
        output <- TestConsole.output
        exp: Vector[String] = Vector("Hello, World!\n")
        ass: Assertion[Vector[String]] = equalTo(exp)
        tr: TestResult = assert(output)(ass)
      } yield tr
    }
  )
}
