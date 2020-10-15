package google.live.t1

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class T1AllNestedChainsPlainSpec extends AnyFunSpec with Matchers {
  import T1Domain._
  import T1Data._
  import T1AllNestedChainsPlain._

  it("fit") {
    val t = Seq(
      ((3,3), (1,1)),
      ((3,3), (2,1)),
      ((3,3), (1,2)),
      ((3,3), (1,1)),
    ).map(_->true)
    val f = Seq(
      ((3,3), (3,1)),
      ((3,3), (3,3)),
      ((3,3), (4,3)),
      ((3,3), (4,4)),
    ).map(_->false)
    for {
      ((bg, sm), r) <- (t ++ f).toMap
    } gt(bg, sm) shouldEqual r
  }

  it("all seq") {
    inscribed(rect9max4) should contain theSameElementsAs List(
      List((101, 1)),
      List((102, 2), (101, 1)),
      List((103, 3), (101, 1)),
      List((103, 3), (102, 2), (101, 1)),
      List((4, 204)),
      List((5, 205), (4, 204)),
      List((11, 11)),
      List((12, 12), (11, 11)),
      List((13, 13), (11, 11)),
      List((13, 13), (12, 12), (11, 11)),
      List((14, 14), (11, 11)),
      List((14, 14), (12, 12), (11, 11)),
      List((14, 14), (13, 13), (11, 11)),
      List((14, 14), (13, 13), (12, 12), (11, 11))
    )
  }

  it("longest4") {
    val r = inscribed(rect9max4)
    longest(r) shouldEqual List((14, 14), (13, 13), (12, 12), (11, 11))
  }

  it("longest2") {
    val r = inscribed(rect9max2.toList)
    longest(r).length shouldEqual 2
  }
}