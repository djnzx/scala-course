package cats101

import cats.data.EitherT
import cats.instances.future._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object C140MonadTransform extends App {

//  type Response[A] = Future[Either[String, A]]

  val powerLevels = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )

  type Response[A] = EitherT[Future, String, A]

  def getPowerLevel(autobot: String): Response[Int] = powerLevels.get(autobot) match {
    case Some(a) => EitherT.right(Future(a))
    case None    => EitherT.left(Future(s"$autobot unreachable"))
  }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = {
    val can: Option[Boolean] = for {
      l1v <- powerLevels.get(ally1)
      l2v <- powerLevels.get(ally1)
    } yield l1v + l2v > 15
    can match {
      case Some(a) => EitherT.right(Future(a))
      case None    => EitherT.left(Future(s"$ally1 + $ally2 isn't enough"))
    }
  }

  def tacticalReport(ally1: String, ally2: String): String = {
    val stack = canSpecialMove(ally1, ally2).value

    Await.result(stack, 1 second) match {
      case Left(msg) => s"Comms error: $msg"
      case Right(true) => s"$ally1 and $ally2 are ready to roll out!"
      case Right(false) => s"$ally1 and $ally2 need a recharge."
    }
  }
}
