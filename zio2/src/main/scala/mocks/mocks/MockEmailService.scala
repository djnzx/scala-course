package mocks.mocks

import mocks.api.EmailService
import zio._
import zio.mock._

object MockEmailService extends Mock[EmailService] {

  // for each method we need to have corresponding `Effect`

  //                        input parameters   error   result
  object Send extends Effect[(String, String), String, Unit]

  val compose: URLayer[Proxy, EmailService] =
    ZLayer {
      for {
        proxy <- ZIO.service[Proxy]
      } yield new EmailService {
        override def send(to: String, body: String): IO[String, Unit] =
          proxy(Send, to, body)
      }
    }
}
