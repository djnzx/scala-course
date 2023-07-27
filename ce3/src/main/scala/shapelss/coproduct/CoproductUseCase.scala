package shapelss.coproduct

import cats.implicits._
import io.circe.{Decoder, DecodingFailure, Encoder, HCursor}
import io.circe.generic.AutoDerivation
import io.circe.syntax.EncoderOps
import shapeless.{:+:, CNil, Coproduct, Poly1}
import shapeless.ops.coproduct.Inject

import java.util.UUID
import scala.reflect.runtime.universe.reify

object CoproductUseCase extends App {

  object Domain {
    case class Ok(token: UUID)

    object Ok {
      def next: Ok = Ok(UUID.randomUUID())
    }

    case class Error(message: String)

    object Error extends AutoDerivation

    case object WrongData {
      implicit val e: Encoder[WrongData.type] = _ => "WrongData".asJson
      implicit val d: Decoder[WrongData.type] = (c: HCursor) =>
        c.value.asString
          .filter(_ == "WrongData")
          .as(WrongData)
          .toRight(DecodingFailure("error", List.empty))
    }

  }

  object DomainCombined {
    import Domain._

    type R = Ok :+: Error :+: WrongData.type :+: CNil

    object R {
      def apply[A](
          a: A
        )(
          implicit inj: Inject[R, A]
        ): R = inj(a)
      implicit def autoLiftMembers[A](
          a: A
        )(
          implicit inj: Inject[R, A]
        ): R = inj(a)
    }
  }

  object Instances {
    import Domain._

    val o1: Ok             = Ok.next
    val o2: Error          = Error("too bad")
    val o3: WrongData.type = WrongData
  }

  object InstancesLiftedManually {
    import DomainCombined.R
    import Instances._

    val r1: R = Coproduct[R](o1)
    val r2: R = Coproduct[R](o2)
    val r3: R = Coproduct[R](o3)
  }

  object LiftingManuallyWithApply {
    import DomainCombined.R
    import Instances._

    val r1: R = R(o1)
    val r2: R = R(o2)
    val r3: R = R(o3)
  }

  object LiftingAutomatically1st {
    import Domain._
    import DomainCombined.R
    import DomainCombined.R._

    val r1: R = Ok.next
    val r2: R = Error("too bad")
    val r3: R = WrongData
  }

  object LiftingManuallyHK {
    import Domain._
    import DomainCombined.R

    // TODO: implement implicit HKT auto-lifting
    val r1: Option[R] = Ok.next.some.map(x => R.apply(x))
    val r2: Option[R] = Error("too bad").some.map(x => R.apply(x))
    val r3: Option[R] = WrongData.some.map(x => R.apply(x))
  }

  object MappingCore {
    import Domain._

    // TODO: now it's a function A => String, implement custom type B
    val foldOk: Ok => String                    = (ok: Ok) => s"Ok: Token: ${ok.token}"
    val foldEr: Error => String                 = (er: Error) => s"Error: Message: ${er.message}"
    val foldWd: Domain.WrongData.type => String = (_: WrongData.type) => "Wrong Data, no details"
  }

  object MappingCombined1 {
    import Domain._
    import MappingCore._

    object folder extends Poly1 {
      implicit val a: folder.Case.Aux[Ok, String]                    = at[Ok](foldOk)
      implicit val b: folder.Case.Aux[Error, String]                 = at[Error](foldEr)
      implicit val c: folder.Case.Aux[Domain.WrongData.type, String] = at[WrongData.type](foldWd)
    }

    import InstancesLiftedManually._

    /** calling R.fold + passing folder explicitly */
    val s1: String = r1.fold(folder)
    val s2: String = r2.fold(folder)
    val s3: String = r3.fold(folder)

    pprint.pprintln(s1)
    pprint.pprintln(s2)
    pprint.pprintln(s3)
  }

  object MappingCombined2 {
    import Domain._
    import MappingCore._

    object folder extends Poly1 {
      implicit val a = at[Ok](foldOk)
      implicit val b = at[Error](foldEr)
      implicit val c = at[WrongData.type](foldWd)
    }

    import Instances._

    // they are string, but we can't write that
    val s1                  = folder(o1)
    val s2                  = folder(o2)
    val s3                  = folder(o3)
    // but can use
    def whatever(s: String) = pprint.pprintln(s)
    whatever(s1)
    whatever(s2)
    whatever(s3)
  }

  object MappingCombined3 {
    import Domain._
    import MappingCore._

    val folder = Poly1
      .at[Ok](foldOk)
      .at[Error](foldEr)
      .at[WrongData.type](foldWd)
      .build
    // import folder._ // Idea always removing this import
    import folder._
    import Instances._
    // we need to import context manually

    // they are string, but we can't write that
    val s1                  = folder(o1)
    val s2                  = folder(o2)
    val s3                  = folder(o3)
    // but can use
    def whatever(s: String) = pprint.pprintln(s)
    // IntelliJ Idea is not OK hare ;)
    whatever(s1)
    whatever(s2)
    whatever(s3)

    import InstancesLiftedManually._
    // even map
    val x = r1.map(folder)
    println(x.unify)
    val y = reify(x)
    println(y)
  }

  MappingCombined1
  MappingCombined2
  MappingCombined3

}
