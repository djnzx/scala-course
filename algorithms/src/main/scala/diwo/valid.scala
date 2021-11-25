package diwo

import diwo.Domain._
import diwo.ExtendedSyntax._

import scala.util.Try

object valid {

  /** generic validator */
  def validate(s: Set[Int])(p: Set[Int] => Boolean) = Some(s).filter(p)

  /** exact size validation */
  def sizeEq(s: Set[Int], sz: Int) =
    validate(s)(_.size == sz)
      .toRight(msg.mustEq(sz, s.size))
      .mapLeft(msg.size)

  /** range validation */
  def sizeBtw(s: Set[Int], mn: Int, mx: Int) =
    validate(s)(s => s.size >= mn && s.size <= mx)
      .toRight(msg.mustBtw(mn, mx, s.size))
      .mapLeft(msg.size)

  /** normal validation: shared in normal ticket and draw */
  def normalValidation(ns: Set[Int], sns: Set[Int]) = for {
    nor <- sizeEq(ns, NC).mapLeft(msg.cn)
    str <- sizeEq(sns, SNC).mapLeft(msg.csn)
  } yield (nor, str)

  /** helper stuff to implement validation */
  def toInt(s: String) = Try(s.toInt).toOption

  /** parse String to Two Sets: Sets delimiter - `/` (slash) Items delimiter - `,` (comma) size of arrays isn't checked,
    * it will be done in appropriate apply() methods
    */
  def parseTwoArrays(s: String) =
    Option(s)
      .map(_.trim)
      .map(_.split("/"))
      .filter(_.length == 2)
      .map(_.map(_.split(",")))
      .map(_.map(_.map(_.trim)))
      .map(_.map(_.flatMap(toInt)))
      .map {
        case Array(a, b) => a.toSet -> b.toSet
        case _           => ???
      }
}
