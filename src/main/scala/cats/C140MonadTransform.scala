package cats

import cats.data.OptionT
import cats.instances.either._
import cats.syntax.applicative._

import scala.concurrent.Future

object C140MonadTransform extends App {

  type Response[A] = Future[Either[String, A]]
  def getPowerLevel(autobot: String): Response[Int] = ???


}
