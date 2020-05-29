package httprq

import java.util.concurrent.{ExecutorService, Executors}

import cats.effect.{Blocker, ContextShift, IO}
import cats.instances.vector._
import cats.syntax.parallel._
import http_book.Book
import org.http4s.client.dsl.io._
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.dsl.io._
import org.http4s.implicits._

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object HttpRqApp5PostEntityProcessResponse extends App {
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  val es5: ExecutorService = Executors.newFixedThreadPool(5)
  val blockingEC = ExecutionContext.fromExecutorService(es5)
  val blocker: Blocker = Blocker.liftExecutionContext(blockingEC)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO](blocker).create

  val books: Vector[Book] = Map(
    "Java" -> "Jim",
    "Scala" -> "Bim"
  ).toVector map { case (n, a) => Book(n, a) }

  /**
    * POST(uri, headers*)
    * POST(body:A, uri, headers*)(impl EntityEncoder[A])
    */
  val rqPostDesc = (b: Book) => POST.apply(
    b,
    uri"http://localhost:8080" / "book",
  )

  def doPostAndDecode(b: Book): IO[Book] = httpClient.expect[Book](rqPostDesc(b))
  // automatic decoding because of type specified              ^^

  val greetingList: IO[Vector[Book]] = books.parTraverse(doPostAndDecode)
  val res: Vector[Book] = greetingList.unsafeRunSync()
  println(res)
  blockingEC.shutdown()

}
