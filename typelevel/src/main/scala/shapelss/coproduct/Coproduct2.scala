package shapelss.coproduct

import io.circe._
import io.circe.generic.semiauto._
import io.circe.shapes._
import io.circe.syntax._
import shapeless._
import shapeless.union._
import shapeless.syntax.singleton._

object Coproduct2 extends App {

  object model {
    case class A(a: String)
    case class B(a: String, i: Int)
    case class C(i: Int, b: Boolean)

    implicit val encodeA: Encoder[A] = deriveEncoder
    implicit val encodeB: Encoder[B] = deriveEncoder
    implicit val encodeC: Encoder[C] = deriveEncoder

    implicit val decodeA: Decoder[A] = deriveDecoder
    implicit val decodeB: Decoder[B] = deriveDecoder
    implicit val decodeC: Decoder[C] = deriveDecoder
  }

  import model._

  type ABC = A :+: B :+: C :+: CNil
  val c: ABC = Coproduct[ABC](C(123, false))

//  println(c) // Inr(Inr(Inl(C(123,false))))
//  println(c.asJson.noSpaces)
  assert { c.asJson.noSpaces == """{"i":123,"b":false}""" }

  val b: ABC = Coproduct[ABC](B("xyz", 123))
  val json = b.asJson

  assert { json.noSpaces == """{"a":"xyz","i":123}""" }

  val decoded = io.circe.jawn.decode[ABC](json.noSpaces)

  assert { decoded == Right(Coproduct[ABC](A("xyz"))) }
  assert { ("xyz" :: List(1, 2, 3) :: false :: HNil).asJson.noSpaces == """["xyz",[1,2,3],false]""" }

  type ABCL = Union.`'A -> A, 'B -> B, 'C -> C`.T

  val bL: ABCL = Coproduct[ABCL]('B ->> B("xyz", 123))
  val jsonL = bL.asJson.noSpaces
  println("------------------")
  println(jsonL)
  println("------------------")
  assert { jsonL == """{"B":{"a":"xyz","i":123}}""" }

  val decodedL = io.circe.jawn.decode[ABCL](jsonL)

  assert { decodedL == Right(b) }
  assert { decodedL == Right(bL) }

  val x = ('a ->> "xyz" :: 'b ->> List(1) :: 'c ->> false :: HNil).asJson.noSpaces

  assert { x == """{"c":false,"b":[1],"a":"xyz"}""" }
}
