package _newage

import cats._
import cats.data._
import cats.implicits._
import java.util.UUID
import scala.util.Try

object PrefixedUuidPlayground extends App {

  abstract class PrefixedUuid(val prefix: String, id: UUID, test: Boolean) {
    override lazy val toString: String = test match {
      case true => s"$prefix$id-test"
      case _    => s"$prefix$id"
    }
  }

  trait Maker[A] {
    def make(uuid: UUID): A
  }

  class PrefixedUUIDOps[A](prefix: String) {

    def deriveMaker: Maker[A] = (x: UUID) => ??? //MakerMacros.createInstanceWithParams[A](x)

    def next(implicit mk: Maker[A]): A = mk.make(UUID.randomUUID())

    def validate(raw: String)(implicit mk: Maker[A]): Validated[String, A] =
      raw.valid
        .ensure(s"should start from `$prefix`")(_.startsWith(prefix))
        .map(_.stripPrefix(prefix))
        .map(_.stripSuffix("-test"))
        .ensure("should be an UUID after prefix")(x => Try(UUID.fromString(x)).isSuccess)
        .map(UUID.fromString)
        .map(uuid => mk.make(uuid))

    def unapply(maybeId: String)(implicit maker: Maker[A]): Option[A] = validate(maybeId).toOption
  }

  case class UserId(uuid: UUID, test: Boolean = false) extends PrefixedUuid("USER-", uuid, test)

  object UserId extends PrefixedUUIDOps[UserId]("USER-") {
    // TODO: can be implemented via macros
    implicit lazy val maker: Maker[UserId] = deriveMaker // (x: UUID) => UserId(x)
  }

  case class CarId(id: UUID, test: Boolean = false) extends PrefixedUuid("CAR-", id, false)

//  val x = UserId.next.copy(test = true)
//  println(x)

//  def make[A: ClassTag] = MakerMacros.createInstanceWithParams[A](UUID.randomUUID())
//
//  val x = make[UserId]
//  println(x)
//  val instance = MacroUtils.createInstance[String]
//  println(instance.getValue) // Output: default

//  Even.isEven(3)
//  val x: Int = Plus.add2(10)
//  println(x)
}
