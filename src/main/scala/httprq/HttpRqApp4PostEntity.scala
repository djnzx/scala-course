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

object HttpRqApp4PostEntity extends App {
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  val es5: ExecutorService = Executors.newFixedThreadPool(5)
  val blockingEC = ExecutionContext.fromExecutorService(es5)
  val blocker: Blocker = Blocker.liftExecutionContext(blockingEC)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO](blocker).create

  val booksMap: Map[String, String] = Map(
    "Java" -> "Jim",
    "Scala" -> "Bim"
  )
  val books: Vector[Book] = booksMap.toVector map { case (n, a) => Book(n, a) }

  /**
    * POST(uri, headers*)
    * POST(body:A, uri, headers*)(impl EntityEncoder[A])
    */
  val rqPostDesc = (b: Book) => POST.apply(
    b,
    uri"http://localhost:8080" / "book",
  )

  def doPost(b: Book): IO[String] = httpClient.expect[String](rqPostDesc(b))

  val greetingList: IO[Vector[String]] = books.parTraverse(doPost)
  val long = System.currentTimeMillis()
  val res: Vector[String] = greetingList.unsafeRunSync()
  println(System.currentTimeMillis() - long)
  println(res)
  blockingEC.shutdown()

}
