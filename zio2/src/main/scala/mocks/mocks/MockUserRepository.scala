package mocks.mocks

import mocks.api.{User, UserRepository}
import zio._
import zio.mock._

object MockUserRepository extends Mock[UserRepository] {

  // for each method we need to have corresponding `Effect`

  //              input parameter  error   result
  object Save extends Effect[User, String, Unit]

  val compose: URLayer[Proxy, UserRepository] =
    ZLayer {
      for {
        proxy <- ZIO.service[Proxy]
      } yield new UserRepository {

        override def save(user: User): IO[String, Unit] =
          proxy(Save, user)

      }
    }
}
