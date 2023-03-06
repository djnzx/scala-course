package sendmail

import cats.effect._
import cats.effect.std._
import cats.implicits._
import jakarta.mail._
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.typelevel.log4cats.Logger
import scala.concurrent.ExecutionContext

trait Mailer[F[_]] {
  def send(subject: String, content: String, recipient: String): F[Unit]
}

object Mailer {
  case class MailConfig(queueSize: Int, user: String, password: String, sender: String, host: String, port: Int)

  def make[F[_]: Async](blockingEC: ExecutionContext, config: MailConfig, logger: Logger[F]): Resource[F, Mailer[F]] = {

    case class Mail(subject: String, content: String, recipient: String)

    def send(queue: Queue[F, Mail], sender: InternetAddress, session: Session): F[Unit] =
      queue.take.flatMap { mail =>
        val message = new MimeMessage(session)
        message.setFrom(sender)
        message.setRecipients(
          Message.RecipientType.TO,
          InternetAddress.parse(mail.recipient).asInstanceOf[Array[Address]]
        )
        message.setSubject(mail.subject)
        message.setContent(mail.content, "text/html")
        message.saveChanges()
        Async[F].start {
          Async[F].blocking(Transport.send(message)).attempt.flatMap {
            case Right(_) => logger.debug(s"Sent message to ${mail.recipient} with subject ${mail.subject}.")
            case Left(x)  => logger.error(s"Can't send message to ${mail.recipient} with subject ${mail.subject}. Exception$x")
          }
        }
      } >> send(queue, sender, session)

    for {
      queue <- Resource.eval { Queue.bounded[F, Mail](config.queueSize) }
      props = System.getProperties
      _ = props.setProperty("mail.smtp.host", config.host)
      _ = props.setProperty("mail.smtp.port", config.port.toString)
      _ = props.setProperty("mail.smtp.auth", true.toString)
      _ = props.setProperty("mail.smtp.starttls.enable", true.toString)
      auth = new Authenticator {
               override protected def getPasswordAuthentication: PasswordAuthentication =
                 new PasswordAuthentication(config.user, config.password)
             }
      session = Session.getInstance(props, auth)
      sender = new InternetAddress(config.sender)
      _     <- Async[F].backgroundOn(
                 send(queue, sender, session),
                 blockingEC
               )
    } yield new Mailer[F] {

      override def send(subject: String, content: String, recipient: String): F[Unit] =
        queue.offer(Mail(subject, content, recipient)) *>
          logger.debug(s"Sending message to $recipient with subject $subject...")

    }
  }
}
