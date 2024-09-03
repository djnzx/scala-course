package mocks.api

import zio.IO

trait UserRepository {
  def save(user: User): IO[String, Unit]
}
