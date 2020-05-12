package httprq

import java.util.concurrent.{ExecutorService, Executors}

import cats.effect.{Blocker, ContextShift, IO}
import cats.instances.vector._
import cats.syntax.parallel._
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.{UrlForm, _}
import org.http4s.circe.jsonEncoderOf
import org.http4s.client.dsl.io._
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.dsl.io._
import org.http4s.implicits._

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object HttpRqApp3PostForm extends App {
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  val es5: ExecutorService = Executors.newFixedThreadPool(5)
  val blockingEC = ExecutionContext.fromExecutorService(es5)
  val blocker: Blocker = Blocker.liftExecutionContext(blockingEC)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO](blocker).create

  /**
    * POST(uri, headers*)
    * POST(body:A, uri, headers*)(impl EntityEncoder[A])
    */
  val rqPostForm = () => POST.apply(
    UrlForm(
      "username" -> "Jim",
      "password" -> "abc123"
    ),
    uri"http://localhost:8080" / "book",
  )

  def doPost(): IO[String] = httpClient.expect[String](rqPostForm())

  val post: IO[String] = doPost()
  post.unsafeRunSync()
  blockingEC.shutdown()
}
