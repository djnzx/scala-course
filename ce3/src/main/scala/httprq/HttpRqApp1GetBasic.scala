package httprq

import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.instances.vector._
import cats.syntax.parallel._
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client._
import org.http4s.implicits._
import org.http4s.{ParseFailure, Response, Uri}

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext

object HttpRqApp1GetBasic extends App {
  /**
    * blaze needs to shutdown after using
    */
  val rqio: IO[Unit] = BlazeClientBuilder[IO].resource.use { client: Client[IO] =>
    // use `client` here and return an `IO`.
    // the client will be acquired and shut down
    // automatically each time the `IO` is run.
    IO.unit
  }

  /**
    * It uses blocking IO and is less suited for production
    * but it is highly useful in a REPL
    */
  val es5: ExecutorService = Executors.newFixedThreadPool(5)
  val blockingEC = ExecutionContext.fromExecutorService(es5)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO].create

  /**
    * base request
    */
  def hello2(name: String): IO[String] = {
    // if we want to handle possibly wrong uri ->
    val urie: Either[ParseFailure, Uri] = Uri.fromString("http://localhost::8080/hello/")
    // actually a compile-time macro
    val uri: Uri = uri"http://localhost:8080/hello/"
    val target: Uri = uri / name
    httpClient.expect[String](target)
  }

  def hello(name: String): IO[String] = httpClient.get[String](
    uri"http://localhost:8080" / "hello" / name
  ) { rs: Response[IO] =>rs.as[String]}

  val people: Vector[String] = Vector("Michael", "Jessica", "Ashley", "Christopher")
  val greetingList: IO[Vector[String]] = people.parTraverse(hello)
  val long = System.currentTimeMillis()
  val res: Vector[String] = greetingList.unsafeRunSync()
  println(System.currentTimeMillis() - long)
  println(res)
  blockingEC.shutdown()

}
