package mocks.api

import zio.IO

trait EmailService {
  //       input parameters             error   result
  def send(to: String, body: String): IO[String, Unit]
}
