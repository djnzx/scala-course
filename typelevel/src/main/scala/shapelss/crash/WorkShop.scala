package shapelss.crash

import shapeless._
import labelled._
import syntax.singleton._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import shapeless.syntax.SingletonOps

object WorkShop {

  val t1 = "hello".narrow
  val t2 = 40.narrow
  val t3 = Symbol("term").narrow
  val t4 = true.narrow
  val t5 = Nil.narrow

  val t6 = 'a ->> "abc"

  val tagged = ('a ->> "hello") :: ('b ->> 13) :: HNil
}

class WorkShopSpec extends AnyFunSpec with Matchers {

  it("1") {}

}
