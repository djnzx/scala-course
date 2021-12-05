package tapirx

import cats.effect.IO
import org.http4s.HttpRoutes

object TapirTeaser {

  // endpoint

  import sttp.tapir._
  import sttp.tapir.generic.auto._
  import sttp.tapir.json.circe._
  import io.circe.generic.auto._

  type Limit = Int
  type AuthToken = String
  case class BooksFromYear(genre: String, year: Int)
  case class Book(title: String)

  val booksListing: PublicEndpoint[(BooksFromYear, Limit, AuthToken), String, List[Book], Any] =
    endpoint
      .get
      .in(("books" / path[String]("genre") / path[Int]("year")).mapTo[BooksFromYear])
      .in(query[Limit]("limit").description("Maximum number of books to retrieve"))
      .in(header[AuthToken]("X-Auth-Token"))
      .errorOut(stringBody)
      .out(jsonBody[List[Book]])

  import sttp.tapir.server.http4s.Http4sServerInterpreter

  // abstract endpoint description
  object Definition {

//                                Endpoint[Unit, String, Unit, Int, Any] =
    val endPointDefinition: PublicEndpoint[ /**/ String, Unit, Int, Any] =
      endpoint
        .post
        .in(stringBody)
        .out(plainBody[Int])

  }

  // real server implementation
  object Implementation {

    def countCharacters(s: String): IO[Either[Unit, Int]] =
      IO.pure(Right[Unit, Int](s.length))

  }

  // bind description to implementation
  object Wiring {

    import Definition._
    import Implementation._

    val countCharactersRoutes: HttpRoutes[IO] =
      Http4sServerInterpreter[IO]()
        .toRoutes(endPointDefinition.serverLogic(countCharacters))

  }

}
