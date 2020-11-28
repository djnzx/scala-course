package nomicon.ch03test

object Timed {
  import zio.ZIO
  import zio.clock._
  import zio.duration._
  import zio.console._
  
  val goShopping: ZIO[Console with Clock, Nothing, Unit] = putStrLn("Going shopping!").delay(1.hour)
}

object TimedTest extends zio.test.DefaultRunnableSpec {
  import zio.test._
  import zio.duration._
  import zio.test.Assertion._
  import zio.test.environment.{TestClock, TestConsole}
  
  def myAssertion(value: Vector[String]) = assertCompletes && assert(value)(equalTo(Vector("Going shopping!\n")))
  
  def spec = suite("timed experiments")(
    testM("delayed test") {
      for {
        fiber <- Timed.goShopping.fork
        _     <- TestClock.adjust(1.hour)
        _     <- fiber.join
        value <- TestConsole.output
      } yield myAssertion(value)
    }
  )
  
}