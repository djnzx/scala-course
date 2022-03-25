package shapelesscoproduct

import cats.implicits.toFunctorOps
import io.circe.Decoder
import io.circe.DecodingFailure
import io.circe.Encoder
import io.circe.HCursor
import io.circe.generic.AutoDerivation
import io.circe.syntax.EncoderOps
import shapeless.ops.coproduct.Inject
import shapeless.:+:
import shapeless.CNil
import shapeless.Generic
import shapeless.Poly1

import java.util.UUID

object UseCase extends App {

  /** different models for output */
  case class Ok(token: UUID)
  object Ok extends AutoDerivation {
    def next = Ok(UUID.randomUUID())
  }
  case class Error(message: String)
  object Error extends AutoDerivation
  case object WrongData {
    implicit val e: Encoder[WrongData.type] = _ => "WrongData".asJson
    implicit val d: Decoder[WrongData.type] = (c: HCursor) =>
      c.value
        .asString
        .filter(_ == "WrongData")
        .as(WrongData)
        .toRight(DecodingFailure("error", List.empty))
  }

  type R = Ok :+: Error :+: WrongData.type :+: CNil
  object R {
    def apply[A](a: A)(implicit inj: Inject[R, A]): R = inj(a)
    implicit def autoLiftMembers[A](a: A)(implicit inj: Inject[R, A]): R = inj(a)
  }

  // constructing
  val o1: Ok = Ok.next
  val o2: Error = Error("too bad")
  val o3: WrongData.type = WrongData

  val r1: R = R(o1)
  val r2: R = R(o2)
  val r3: R = R(o3)

  // manual lifting
  def service1(n: Int): R = n match {
    case 1 => R(Ok.next)
    case 2 => R(Error("too bad"))
    case _ => R(WrongData)
  }

  // automatic lifting
  import R.autoLiftMembers
  def service2(n: Int): R = n match {
    case 1 => Ok.next
    case 2 => Error("too bad")
    case _ => WrongData
  }

  val foldOk = (ok: Ok) => s"Ok: Token: ${ok.token}"
  val foldEr = (er: Error) => s"Error: Message: ${er.message}"
  val foldWd = (_: WrongData.type) => "Wrong Data, no details"

  object folder extends Poly1 {
    implicit val a = at[Ok](foldOk)
    implicit val b = at[Error](foldEr)
    implicit val c = at[WrongData.type](foldWd)
  }

  val s1: String = r1.fold(folder)
  val s2: String = r2.fold(folder)
  val s3: String = r3.fold(folder)

  val folder2 = Poly1
    .at[Ok](foldOk)
    .at[Error](foldEr)
    .at[WrongData.type](foldWd)
    .build

  val x = Generic[R].to(r1)

  pprint.pprintln(s1)
  pprint.pprintln(s2)
  pprint.pprintln(s3)
}
