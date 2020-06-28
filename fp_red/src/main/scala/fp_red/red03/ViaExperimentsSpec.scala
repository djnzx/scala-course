package fp_red.red03

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class ViaExperimentsSpec extends AnyFunSpec with Matchers {
  import fp_red.red03.ViaExperiments._
  describe("via") {
    import scala.collection.immutable.List
    
    describe("map via fold") {
      it("1") {
        val inc = (x:Int) => x + 1
        val list = List(1,2,3)
        mapViaFoldRight(list)(inc) shouldBe List(2,3,4)
      }
      it("2") {
        val len = (s:String) => s.length
        val list = List("a", "bcd", "ef")
        mapViaFoldRight(list)(len) shouldBe List(1,3,2)
      }
    }
    describe("fold to func") {
      it("length") {
        def len[A](a: A, acc: Int): Int = acc + 1
        val list = List(1,2,3,44,55)
        
        foldViaFoldRightToFunc2(list)(0)(len) shouldBe 5
      }
      it("sum") {
        def sum(a: Int, acc: Int): Int = acc + a
        val list = List(1,2,3,44,55)
        
        foldViaFoldRightToFunc2(list)(0)(sum) shouldBe 105
      }
      it("product") {
        def product(a: Int, acc: Int): Int = acc * a
        val list = List(1,3,5,7)
        
        foldViaFoldRightToFunc2(list)(1)(product) shouldBe 105
      }
    }
    describe("switch") {
      it("1") {
        switch(1) shouldBe 0
      }
      it("2") {
        switch(0) shouldBe 1
      }
      it("3") {
        switch1(true) shouldBe false
      }
      it("4") {
        switch1(false) shouldBe true
      }
      it("5") {
        switch1(true) shouldBe false
      }
      it("6") {
        switch1(false) shouldBe true
      }
    }
  }
  describe("varargs1") {
    it("1") {
      varargs1() shouldBe 0
    }
    it("2") {
      varargs1(1) shouldBe 1
    }
    it("3") {
      varargs1(1, 2, 3, 4) shouldBe 4
    }
  }
  describe("varargs2") {
    it("1") {
      varargs2(Seq()) shouldBe 0
    }
    it("2") {
      varargs2(Seq(1)) shouldBe 1
    }
    it("3") {
      varargs2(Seq(1,2,3,4)) shouldBe 4
    }
  }
}
