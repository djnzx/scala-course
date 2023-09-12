package _newage

import cats.Show
import cats.implicits._
import java.util.UUID
import scala.util.Try

object PrefixedUuidPlayground2 extends App {

  private trait IdExtractor[A] extends (A => String)

  abstract class IdBase[A](private val prefix: String, private val idExtractor: IdExtractor[A], private val testSuffix: String = "-test") {
    implicit val show: Show[A] = (a: A) => s"$prefix${idExtractor(a)}"

    // uuid + test
    def unapply(raw: String): Option[(UUID, Boolean)] =
      Option(raw)
        .collect { case s if s.startsWith(prefix) => s.substring(prefix.length) }
        .collect {
          case s if s.endsWith(testSuffix) => s.substring(0, s.length - testSuffix.length) -> true
          case s                           => s                                            -> false
        }
        .flatMap { case (maybeUuid, isTest) => Try(UUID.fromString(maybeUuid)).toOption.map(_ -> isTest) }

    def is(raw: String): Boolean = unapply(raw).fold(false)(_ => true)
  }

  // not the best approach, but cleaner
  implicit class MakeTestOps[A: Show](a: A) {
    def test: String = implicitly[Show[A]].show(a) + "-test"
  }

  case class CarId(id: UUID) extends AnyVal
  object CarId               extends IdBase[CarId]("CAR-", _.id.toString)

  // TODO: how to create a test entity

  // usecase
  val carId = CarId(UUID.randomUUID())
  val s = carId.show
  println(s)
  val st = carId.test
  println(st)

  // matching
  def check(raw: String) = raw match {
    case CarId(id) => id.asRight
    case _         => "not a car id".asLeft
  }

  Seq(
    s,
    s+"-test",
    "whatever"
  ).map(x => check(x) -> CarId.is(x))
    .foreach(x => pprint.pprintln(x))
}
