package httpfs2.serv

import cats.Applicative
import cats.syntax.applicative._     // .pure
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

trait ServiceA[F[_]] {
  def get:  F[ServiceA.ResponseA1]
  def get2: F[ServiceA.ResponseA2]
}

object ServiceA {
  // fixing response type
  case class ResponseA1(a: List[String])
  type ResponseA2 = List[String]

  // implementation, can be way more complicated
  val allItems1: ResponseA1 = ResponseA1(List("A", "B", "C"))
  val allItems2: ResponseA2 = List("A", "B", "C")

  // encoder to convert our result to JSON must be in
  // the corresponding object
  object ResponseA1 {
    implicit val a1Encoder: Encoder[ResponseA1] = deriveEncoder
    implicit def a1EntityEncoder[F[_]: Applicative]: EntityEncoder[F, ServiceA.ResponseA1] = jsonEncoderOf
  }

  // implementing our contract
  def impl[F[_]: Applicative]: ServiceA[F] = new ServiceA[F] {
    override def get: F[ResponseA1] = {
      // call our implementation
      val result = allItems1
      // lift to F in order to be able to flatmap it further
      result.pure[F]
    }

    override def get2: F[ResponseA2] = {
      // call our implementation
      allItems2
        // lift to F in order to be able to flatmap it further
        .pure[F]
    }
  }
}


