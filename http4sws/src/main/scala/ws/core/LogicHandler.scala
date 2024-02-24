package ws.core

import cats.effect.kernel.Ref
import cats.syntax.all.*

trait LogicHandler[F[_]] {
  def parse(
      userRef: Ref[F, Option[User]],
      text: String
    ): F[List[OutputMsg]]
}

case class TextCommand(left: String, right: Option[String])

