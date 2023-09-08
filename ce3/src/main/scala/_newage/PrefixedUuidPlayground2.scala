package _newage

import cats.implicits._

import java.util.UUID
import scala.util.Try
import cats.Show

object PrefixedUuidPlayground2 extends App {

  private trait IdExtractor[A] extends (A => String)

  abstract class IdBase[A](private val prefix: String, private val idFn: IdExtractor[A]) {
    implicit val show: Show[A] = (a: A) => s"$prefix${idFn(a)}"

    def unapply(raw: String): Option[UUID] =
      Option(raw)
        .collect { case s if s.startsWith(prefix) => s.substring(prefix.length) }
        .flatMap(s => Try(UUID.fromString(s)).toOption)

    def is(raw: String): Boolean = unapply(raw).fold(false)(_ => true)
  }

  case class CarId(id: UUID) extends AnyVal
  object CarId extends IdBase[CarId]("CAR-", _.id.toString)

  // usecase
  val s = CarId(UUID.randomUUID()).show
  println(s)

  // matching
  def check(raw: String): Either[String, UUID] = raw match {
    case CarId(id) => id.asRight
    case _ => "not a car id".asLeft
  }

  Seq(
    s,
    "whatever"
  ).map(x => check(x) -> CarId.is(x))
    .foreach(println)
}
