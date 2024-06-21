package djnzx.tcder

import cats.syntax.all.*
import scala.Tuple.Union
import scala.compiletime as ct
import scala.deriving.Mirror

/** Type class derivation
  *
  * Noel Welsh masterclass & pair coding 19.10.2023
  */
trait Decoder[A]:
  def decode(raw: Map[String, String]): Option[A]

trait Value[A]:
  def decode(raw: String): Option[A]

object Value:
  given Value[Int] with
    def decode(raw: String): Option[Int] = raw.toIntOption

  given Value[String] with
    def decode(raw: String): Option[String] = raw.some

object Decoder:
  inline def derived[A](using m: Mirror.Of[A]): Decoder[A] =
    inline m match
      case s: Mirror.SumOf[A]     => ???
      case p: Mirror.ProductOf[A] =>
        (raw: Map[String, String]) =>
          type MPT = Tuple.Map[p.MirroredElemTypes, Value] // (String, Int)
          val elemNames: p.MirroredElemLabels = ct.constValueTuple[p.MirroredElemLabels]
          val elemValues: MPT = ct.summonAll[MPT]

          (elemNames.toList zip elemValues.toList)
            .asInstanceOf[List[(String, Value[?])]]
            .map { case (name, param) => raw.get(name).flatMap(param.decode) }
            .sequence
            .map(v => p.fromTuple(Tuple.fromArray(v.toArray).asInstanceOf[p.MirroredElemTypes]))

final case class User(name: String, score: Int) derives Decoder

@main def main() =
  val raw = Map("name" -> "Tom", "score" -> "24")

  val d1: Decoder[User] = summon[Decoder[User]] // implicitly
  pprint.log(d1.decode(raw))

  val d2: Decoder[User] = Decoder.derived[User] // direct macro call
  pprint.log(d2.decode(raw))
