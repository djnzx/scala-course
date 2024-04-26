package alexr.explore.domain

import cats.Applicative
import cats.implicits._

trait Quotes[F[_]] {
  def next: F[String]
}

object Quotes {

  class Impl[F[_]: Applicative] extends Quotes[F] {
    override def next: F[String] = QuotesData.next.pure[F]
  }

}
