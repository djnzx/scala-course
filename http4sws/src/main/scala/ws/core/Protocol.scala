package ws.core

import cats.MonadThrow
import cats.effect.Ref
import cats.effect.Sync
import cats.implicits._
import ws.core.OutputMsg.OutputMessage
import ws.core.OutputMsg.OutputMessage._

/** the ultimate idea of protocol is to isolate `Ref` manipulation */
trait Protocol[F[_]] {
  // modifying app/user state
  def login(user: User, userState: => UserState[F]): F[Protocol.Outcome]
  def logout(): F[Protocol.Outcome]
  def disconnect(): F[Protocol.Outcome]
  // reading app/user state
  def help: F[Protocol.Outcome]
  def sendPublic(msg: String): F[Protocol.Outcome]
  def sendPrivate(msg: String, to: String): F[Protocol.Outcome]
  // aren't accessing app/user state
  def respond(message: String): F[Protocol.Outcome]
  def ignore: F[Protocol.Outcome]
}

object Protocol {
  type Outcome = (List[OutputMessage], List[String])

  // TODO: think about implementing two different versions for registered/non-registered users
  def make[F[_]: Sync: MonadThrow](chatStateRef: Ref[F, ChatState[F]])(userRef: Ref[F, Option[User]]): Protocol[F] = new Protocol[F] {

    override def login(user: User, userState: => UserState[F]): F[Outcome] =
      userRef.get.flatMap { // ensure user is not logged in
        case Some(user) =>
          val ms = NeedToLogout(user).pure[List]
          val l1 = s"already logged in, pls logout: $user".pure[List]
          (ms, l1).pure[F].widen
        case None       =>
          chatStateRef
            .modify(_.withUser(user, userState)) // modify chat state
            .flatMap {
              case true  =>
                val ms = UserLoggedIn(user).pure[List]
                val l1 = s"user logged in $user".pure[F]
                val l2 = userRef.get.map(_.toString)
                val l3 = chatStateRef.get.map(_.users.toString)
                userRef.update(_ => Some(user)) >> // modify per-connection user status
                  List(l1, l2, l3).sequence.map(ms -> _)
              case false =>
                val ms = NameIsOccupied(user).pure[List]
                val l1 = s"name $user is already in use".pure[List]
                (ms, l1).pure[F].widen
            }
      }

    override def logout(): F[Outcome] =
      userRef.get.flatMap {
        case None           => (List.empty, "nobody has logged in".pure[List]).pure[F].widen
        case u @ Some(user) =>
          val ms = UserLoggedOut(user).pure[List]
          val l1 = s"user logged out $user".pure[F]
          val l2 = userRef.get.map(_.toString)
          val l3 = chatStateRef.get.map(_.users.toString)
          chatStateRef
            .modify(_.withoutUser(user))
            .flatMap {
              case true  =>
                userRef.update(_ => None) >> // modify per-connection user status
                  List(l1, l2, l3).sequence.map(ms -> _)
              case false =>
                val ms = CommandError(u, "wrong state !!!").pure[List]
                val l1 = "wrong state !!!".pure[List]
                (ms, l1).pure[F].widen
            }

      }

    override def disconnect(): F[Outcome] = ???

    override def help: F[Outcome] =
      userRef.get.flatMap {
        case None    =>
          val text =
            """available commands:
              |  /help
              |  /login {username}
              |""".stripMargin
          (Help(text).pure[List], "/help handled".pure[List]).pure[F].widen
        case Some(_) =>
          val text =
            """available commands:
              |  /help
              |  /logout
              |  {message}
              |  @to {message}
              |""".stripMargin
          (Help(text).pure[List], "/help handled".pure[List]).pure[F].widen
      }

    override def sendPublic(msg: String): F[Outcome] = ???

    override def sendPrivate(msg: String, to: String): F[Outcome] = ???

    /** general private response */
    override def respond(message: String): F[Outcome] = ???

    override def ignore: F[Outcome] =
      (List.empty, List.empty).pure[F].widen

  }
}
