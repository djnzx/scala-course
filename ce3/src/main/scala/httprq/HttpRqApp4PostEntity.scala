package httprq

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.instances.vector._
import cats.syntax.parallel._
import http_book.Book
import org.http4s.client.dsl.io._
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.dsl.io._
import org.http4s.implicits._

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext

object HttpRqApp4PostEntity extends App {
  val es5: ExecutorService = Executors.newFixedThreadPool(5)
  val blockingEC = ExecutionContext.fromExecutorService(es5)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO].create

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
