package httprq

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.instances.vector._
import cats.syntax.parallel._
import org.http4s.client.dsl.io._
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.dsl.io._
import org.http4s.headers.{Accept, Authorization}
import org.http4s.implicits._
import org.http4s.{AuthScheme, Credentials, MediaType}

object HttpRqApp2GetWithHeadersParams extends App {
  val httpClient: Client[IO] = JavaNetClientBuilder[IO].create

  object PCountry extends QueryParamDecoderMatcher[String]("country")

  /** GET(uri, headers*) */
  val rqGetDesc = (name: String) =>
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
}
