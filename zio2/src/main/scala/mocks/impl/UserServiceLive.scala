package mocks.impl

import mocks.api.EmailService
import mocks.api.User
import mocks.api.UserRepository
import mocks.api.UserService
import zio._

case class UserServiceLive(
  emailService: EmailService,
  userRepository: UserRepository
) extends UserService {

  override def register(username: String, age: Int, email: String): IO[String, Unit] =
    if (age < 18) {
      emailService.send(email, "You are not eligible to register!")
    } else if (username == "admin") {
      ZIO.fail("The admin user is already registered!")
    } else {
      for {
        _ <- userRepository.save(User(username, age, email))
        _ <- emailService.send(email, "Congratulation, you are registered!")
      } yield ()
    }
}

object UserServiceLive {

  // we bring all the parameters to
  val layer: URLayer[EmailService with UserRepository, UserService] =
    ZLayer.fromFunction(UserServiceLive.apply _)

}
