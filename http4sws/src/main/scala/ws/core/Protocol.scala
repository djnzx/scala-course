package ws.core

import cats.MonadThrow
import cats.effect.Ref
import cats.effect.Sync
import cats.effect.std.Queue
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
  def respondBack(message: String): F[Protocol.Outcome]
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

    private def doLogout(user: User): F[Outcome] = {
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
            val text = s"user `$user`, wrong state !!!"
            val ms = PrivateMessage(text).pure[List]
            val l1 = text.pure[List]
            (ms, l1).pure[F].widen
        }
    }

    override def logout(): F[Outcome] =
      userRef.get.flatMap {
        case None       =>
          val m = PrivateMessage("to logout you need to be logged in")
          val l = "nobody has logged in"
          (List(m), List(l)).pure[F].widen
        case Some(user) => doLogout(user)
      }

    override def disconnect(): F[Outcome] =
      userRef.get.flatMap {
        case None       =>
          val l = "non-logged user disconnected"
          (List.empty, List(l)).pure[F].widen
        case Some(user) => doLogout(user)
      }

    override def help: F[Outcome] =
      userRef.get.flatMap {
        case None    =>
          val text =
            """available commands:
              |  /help
              |  /login {username}
              |""".stripMargin
          (PrivateMessage(text).pure[List], "/help handled".pure[List]).pure[F].widen
        case Some(_) =>
          val text =
            """available commands:
              |  /help
              |  /logout
              |  {message}
              |  @to {message}
              |""".stripMargin
          (PrivateMessage(text).pure[List], "/help handled".pure[List]).pure[F].widen
      }

    override def sendPublic(msg: String): F[Outcome] =
      userRef.get.flatMap {
        case None       =>
          val text = "must be logged in to send message"
          val m = PrivateMessage(text)
          val l1 = text
          (m.pure[List], l1.pure[List]).pure[F].widen
        case Some(user) =>
          val ms = PublicMessage(msg, user).pure[List]
          val l1 = s"public message from:${user.value} $msg".pure[List]
          (ms, l1).pure[F].widen
      }

    override def sendPrivate(msg: String, to: String): F[Outcome] =
      userRef.get.flatMap {
        case None           => ??? // TODO: not logged
        case Some(userFrom) =>
          val userTo = User(to)
          chatStateRef.get.map(_.findUserState(userTo)).flatMap {
            case None        => ??? // TODO: target not found
            case Some(state) =>
              val ms = DirectMessage(msg, userFrom, userTo)
              val l1 = s"direct message `$msg` from `${userFrom.value}`to `$to` prepared to send"
              val queue: Queue[F, OutputMsg] = state.queue // TODO think how to pass it out here
              (List(ms), List(l1)).pure[F].widen
          }
      }

    override def respondBack(message: String): F[Outcome] = {
      val ms = PrivateMessage(message).pure[List]
      val l1 = message.pure[List]
      (ms, l1).pure[F].widen
    }

    override def ignore: F[Outcome] =
      (List.empty, "ignored".pure[List]).pure[F].widen

  }
}
