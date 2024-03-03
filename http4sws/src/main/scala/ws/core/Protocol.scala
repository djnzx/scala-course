package ws.core

import cats.MonadThrow
import cats.effect.Ref
import cats.effect.Sync
import cats.implicits._
import ws.core.OutputMsg.OutputMessage.NameIsOccupied
import ws.core.OutputMsg.OutputMessage.NeedToLogout
import ws.core.OutputMsg.OutputMessage.UserRegistered

trait Protocol[F[_]] {
  def login(userRef: Ref[F, Option[User]], kv: (User, UserState[F])): F[(List[(OutputMsg, NotificationType)], List[String])]
  def logout(): F[(List[(OutputMsg, NotificationType)], List[String])]
}

object Protocol {
  def make[F[_]: Sync: MonadThrow](chatStateRef: Ref[F, ChatState[F]]): Protocol[F] = new Protocol[F] {

    override def login(userRef: Ref[F, Option[User]], kv: (User, UserState[F])): F[(List[(OutputMsg, NotificationType)], List[String])] =
      kv match {
        case (user, userState) =>
          userRef.get.flatMap { // ensure user is not logged in
            case Some(user) =>
              val cms = (NeedToLogout(user) -> NotificationType.User).pure[List]
              val l1 = s"already logged in, pls logout: $user".pure[List]
              (cms, l1).pure[F].widen
            case None       =>
              chatStateRef
                .modify(_.withUserIf(user, userState)) // modify chat state
                .flatMap {
                  case true  =>
                    val cms = (UserRegistered(user) -> NotificationType.Chat).pure[List]
                    val l1 = s"user logged in $user".pure[F]
                    val l2 = userRef.get.map(_.toString)
                    val l3 = chatStateRef.get.map(_.users.toString)
                    userRef.update(_ => Some(user)) >> // modify per-connection user status
                      List(l1, l2, l3).sequence.map(cms -> _)
                  case false =>
                    val cms = (NameIsOccupied(user) -> NotificationType.User).pure[List]
                    val l1 = s"name $user is already in use".pure[List]
                    (cms, l1).pure[F].widen
                }
          }

      }

    override def logout(): F[(List[(OutputMsg, NotificationType)], List[String])] =
      ???

  }
}
