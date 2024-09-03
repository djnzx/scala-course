package mocks.api

import zio.IO
import zio.ZIO

trait UserService {
  def register(username: String, age: Int, email: String): IO[String, Unit]
}

object UserService {

  // function A => ZIO[_, _, B] from the trait T
  // lifted to:
  // A => ZIO[T, _, B]

  def register(
    username: String,
    age: Int,
    email: String
  ): ZIO[UserService, String, Unit] =
    ZIO.serviceWithZIO { (s: UserService) =>
      s.register(username, age, email)
    }

}
