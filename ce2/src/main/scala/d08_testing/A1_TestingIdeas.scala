package d08_testing

import cats.effect.IO

object A1_TestingIdeas {

  trait EmailAddress
  trait Email

  /** instead of */
  def send(to: EmailAddress, email: Email): IO[Unit] = ???

  /** we do */
  trait EmailDelivery {
    def send(to: EmailAddress, email: Email): IO[Unit] = ???
  }

  /** normal implementation to use in app */
  final class NormalEmailDelivery extends EmailDelivery

  /** fake impl #1 to use in tests */
  final class FakeEmailDelivery extends EmailDelivery

  /** fake impl #2 to use in tests */
  final class FailingEmailDelivery extends EmailDelivery {
    override def send(to: EmailAddress, email: Email): IO[Unit] =
      IO.raiseError(new RuntimeException(s"couldn't send email to $to"))
  }

  /** we pass interface instead of implementation */
  class UserRegistration(emailDelivery: EmailDelivery) {

    def register(email: EmailAddress): IO[Unit] =
      for {
        _ <- save(email)
        _ <- emailDelivery.send(email, new Email {})
      } yield ()

    private def save(email: EmailAddress): IO[Unit] = ???

  }

}
