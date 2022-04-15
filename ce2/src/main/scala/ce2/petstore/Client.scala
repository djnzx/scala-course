package ce2.petstore

import ce2.common.debug.DebugHelper
import cats.effect._
import org.http4s._
import org.http4s.ember.client.EmberClientBuilder

object Client extends IOApp {
  val scruffles = Pet("Scruffles", "dog")

  def run(args: List[String]): IO[ExitCode] =
    pets.use { pets =>
      for {
        id <- pets.give(scruffles)
        pet <- pets.find(id)
        _ <- IO(pet == Some(scruffles)).debug
      } yield ExitCode.Success
    }

  def pets: Resource[IO, PetService[IO]] =
    for {
      client <- EmberClientBuilder.default[IO].build
    } yield ClientResources.pets(client, Uri.uri("http://localhost:8080"))
}
