package app

import io.circe.Decoder
import org.http4s.{ParseFailure, QueryParamDecoder}

object SealedTraitDecoder {

  implicit class EitherExtraSyntax[E, A](private val e: Either[E, A]) extends AnyVal {

    /** just map the left part */
    def leftMap[E2](f: E => E2): Either[E2, A] = e match {
      case Left(e)  => Left(f(e))
      case Right(a) => Right(a)
    }

    /** map left part, but only conforming to certain criteria */
    def leftRefine[E2 <: E](f: E => E2): Either[E2, A] = leftMap(f)
    def leftWiden[E2 >: E](f: E => E2): Either[E2, A] = leftMap(f)
  }

  private val q = "\""
  def wrap(s: String) = s"$q$s$q"

  implicit def customQueryParamDecoder[A: Decoder] =
    QueryParamDecoder[String].emap { x =>
      io.circe.parser.decode[A](wrap(x))
        .leftMap { ex =>
          ParseFailure(x, ex.getMessage)
        }
    }

}
