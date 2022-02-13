package shapelss.crash

import shapeless._
import labelled._
import syntax.singleton._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless.syntax.SingletonOps

object WorkShop extends App {

  /**   - x: Int = 40
    *   - y = 40.narrow
    */
  val t1 = "hello".narrow
  val t2 = 40.narrow // type 40 (singleton)
  val t3 = Symbol("term").narrow
  val t4 = true.narrow
  val t5 = Nil.narrow

  // attaching information available only in compile time, actually kaey ->> value pair
  val t6 = 'a ->> "abc" // String with KeyTag[Symbol with tag.Tagged["a"], String] = "abc"
  val t7 = 11 ->> "abc" // String with KeyTag[11, String] = "abc"

  val hlist: Int :: Boolean :: Double :: HNil =
    1 :: true :: 2.13 :: HNil

  val tagged = ('a ->> "hello") :: ('b ->> 13) :: HNil
  // String with KeyTag[Symbol with tag.Tagged["a"], String] ::
  // Int with KeyTag[Symbol with tag.Tagged["b"], Int] ::
  // HNil =
  //
  val hlist2 = "hello" :: 13 :: HNil

}

class WorkShopSpec extends AnyFunSpec with Matchers {

  it("1") {}

}
