package httprq

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.http4s.UrlForm
import org.http4s.client.dsl.io._
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.dsl.io._
import org.http4s.implicits._

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext

object HttpRqApp3PostForm extends App {
  val es5: ExecutorService = Executors.newFixedThreadPool(5)
  val blockingEC = ExecutionContext.fromExecutorService(es5)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO]create

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
