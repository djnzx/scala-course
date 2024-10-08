package ziomock

import zio._
import zio.mock._
import zio.stream._
import zio.test.Assertion
import zio.test.Spec
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.test.assertTrue

trait ExampleService {

  // effect
  def exampleEffect(i: Int): Task[String]

  // method
  def exampleMethod(): String

  def exampleSink(a: Int): Sink[Throwable, Int, Nothing, List[Int]]
  def exampleStream(a: Int): Stream[Throwable, String]

}

object ExampleService {

  def exampleEffect(i: Int): ZIO[ExampleService, Throwable, String] =
    ZIO.serviceWithZIO[ExampleService] { s =>
      s.exampleEffect(i)
    }

}

object MockExampleService extends Mock[ExampleService] {

  // capability tags
  object ExampleEffect extends Effect[Int, Throwable, String]
  object ExampleMethod extends Method[Unit, Throwable, String]
  object ExampleSink   extends Sink[Any, Throwable, Int, Nothing, List[Int]]
  object ExampleStream extends Stream[Int, Throwable, String]

  override val compose: URLayer[Proxy, ExampleService] = ZLayer {
    ZIO.serviceWithZIO[Proxy] { proxy =>
      withRuntime[Proxy, ExampleService] { runtime =>
        ZIO.succeed {
          new ExampleService {

            override def exampleEffect(i: Int): Task[String] =
              proxy(ExampleEffect, i)

            override def exampleMethod(): String =
              Unsafe.unsafe { implicit unsafe =>
                runtime.unsafe.run(
                  proxy(ExampleMethod, ())
                ).getOrThrow()
              }

            override def exampleSink(a: Int): stream.Sink[Throwable, Int, Nothing, List[Int]] =
              Unsafe.unsafe { implicit unsafe =>
                runtime.unsafe.run(
                  proxy(ExampleSink, a)
                ).getOrThrow()
              }

            override def exampleStream(a: Int): stream.Stream[Throwable, String] =
              Unsafe.unsafe { implicit unsafe =>
                runtime.unsafe.run(
                  proxy(ExampleStream, a)
                ).getOrThrow()
              }

          }
        }
      }
    }

  }
}

object Calc {
  def calc(x: Int): ZIO[ExampleService, Throwable, String] = ExampleService.exampleEffect(x)
}

object ExampleServiceTest extends ZIOSpecDefault {

  def test1 = test("1") {
    // our function to test
    val app: ZIO[ExampleService, Throwable, String] = ExampleService.exampleEffect(33)

    // expectation is a layer
    val exampleServiceLayer: Expectation[ExampleService] = MockExampleService.ExampleEffect(
      assertion = Assertion.equalTo(33),
      result = Expectation.value("33")
    )

    Calc.calc(33)
      .provide(exampleServiceLayer)
      .tap(r => ZIO.succeedBlocking(pprint.log(r)))
      .as(assertTrue(true))
  }

  override def spec: Spec[TestEnvironment, Any] = suite("0")(
    test1,
  )
}
