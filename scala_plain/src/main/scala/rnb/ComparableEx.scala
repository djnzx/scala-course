package rnb

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object Scala1 {
  def cmp[A: Ordering](x: A, y: A): Int =
    implicitly[Ordering[A]].compare(x,y)
}

object Scala2 {
  def cmp[A](x: A, y: A)(implicit oa: Ordering[A]): Int =
    oa.compare(x,y)
}

class ComparableExSpec extends AnyFunSpec with Matchers {
  describe("Scala approach") {
    import Scala2._
    
    it("<"){
      cmp(1,2) shouldEqual -1
    }
    it(">"){
      cmp(20,10) shouldEqual 1
    }
    it("="){
      cmp(3,3) shouldEqual 0
    }
  }
  
  describe("Java approach") {
  }
    
}
