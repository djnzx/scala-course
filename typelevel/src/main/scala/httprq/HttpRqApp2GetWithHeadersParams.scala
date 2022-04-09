package httprq

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import cats.effect.Blocker
import cats.effect.ContextShift
import cats.effect.IO
import cats.instances.vector._
import cats.syntax.parallel._
import org.http4s.client.dsl.io._
import org.http4s.client.Client
import org.http4s.client.JavaNetClientBuilder
import org.http4s.dsl.io._
import org.http4s.headers.Accept
import org.http4s.headers.Authorization
import org.http4s.implicits._
import org.http4s.AuthScheme
import org.http4s.Credentials
import org.http4s.MediaType
import org.http4s.Request

import scala.concurrent.ExecutionContext.global
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContextExecutorService

object HttpRqApp2GetWithHeadersParams extends App {
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  val es5: ExecutorService = Executors.newFixedThreadPool(5)
  val blockingEC = ExecutionContext.fromExecutorService(es5)
  val blocker: Blocker = Blocker.liftExecutionContext(blockingEC)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO](blocker).create

  object PCountry extends QueryParamDecoderMatcher[String]("country")

  /** GET(uri, headers*) */
  val rqGetDesc: String => IO[Request[IO]] = (name: String) =>
    GET.apply(
      uri"http://localhost:8080" / "hello" / name +? ("x", 5) +? ("y", 1),
      Authorization(Credentials.Token(AuthScheme.Bearer, "open sesame")),
      Accept(MediaType.application.json),
    )

  def doGet(name: String): IO[String] = httpClient.expect[String](rqGetDesc(name))

  val people: Vector[String] = Vector("Michael", "Jessica", "Ashley", "Christopher")
  val greetingList: IO[Vector[String]] = people.parTraverse(doGet)
  val long = System.currentTimeMillis()
  val res: Vector[String] = greetingList.unsafeRunSync()
  println(System.currentTimeMillis() - long)
  println(res)
  blockingEC.shutdown()
}
