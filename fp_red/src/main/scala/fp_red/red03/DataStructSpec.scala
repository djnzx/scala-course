package fp_red.red03

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class DataStructSpec extends AnyFunSpec with Matchers {
  describe("data structures") {
    describe("list") {
      
      describe("sum recursive") {
        it("1") {
          List.sum(List(1,2,3,4)) shouldBe 10
        }
        it("0") {
          List.sum(Nil) shouldBe 0
        }
      }
      describe("sum tail-recursive") {
        it("1") {
          List.sumTR(List(1,2,3,4)) shouldBe 10
        }
        it("0") {
          List.sumTR(Nil) shouldBe 0
        }
      }
      describe("product recursive") {
        it("0") {
          the [RuntimeException] thrownBy List.product(Nil)
        }
        it("1") {
          List.product(List(1,2,3,4)) shouldBe 24
        }
        it("2") {
          List.product(List(1,2,0,3,4)) shouldBe 0
        }
      }
      describe("product tail-recursive") {
        it("0") {
          the [RuntimeException] thrownBy List.productTR(Nil)
        }
        it("1") {
          List.productTR(List(1,2,3,4)) shouldBe 24
        }
        it("2") {
          List.productTR(List(1,2,0,3,4)) shouldBe 0
        }
      }
      describe("drop") {
        it("1") {
          List.drop(List(), 100) shouldBe List()
        }
        it("2") {
          List.drop(List(1,2,3,4,5), 0) shouldBe List(1,2,3,4,5)
        }
        it("3") {
          List.drop(List(1,2,3,4,5), 2) shouldBe List(3,4,5)
        }
      }
      describe("length") {
        it("1") {
          List.length(List()) shouldBe 0
        }
        it("2") {
          List.length(List(1,2,3,4,5)) shouldBe 5
        }
      }
      describe("map") {
        it("1") {
          List.map[Int, Int](List()){ _ + 1} shouldBe List()
        }
        it("2") {
          List.map[Int, String](List()){ _.toString } shouldBe List()
        }
        it("3") {
          List.map[Int, String](List(1, 2)){ _.toString } shouldBe Cons("1", Cons("2", Nil))
        }
      }
      describe("reverse") {
        it("1") {
          List.reverse(List()) shouldBe Nil
        }
        it("2") {
          List.reverse(List(1)) shouldBe Cons(1, Nil)
        }
        it("3") {
          List.reverse(List(1, 2)) shouldBe Cons(2, Cons(1, Nil))
        }
      }
      describe("dropLast") {
        it("1") {
          the [RuntimeException] thrownBy List.init(List()) should have message "drop last on empty list"
        }
        it("2") {
          List.init(List(1)) shouldBe Nil
        }
        it("3") {
          List.init(List(1,2,3)) shouldBe Cons(1, Cons(2, Nil))
        }
      }
      describe("foldRight") {
        it("1") {
          List.foldRight(
            List(1,2,3,4), List[Int]()
          ) { (a: Int, acc: List[Int]) => 
            Cons[Int](a, acc) 
          } shouldBe Cons(1, Cons(2, Cons(3, Cons(4, Nil))))
        }
      }
    }
    describe("foldRightViaFoldLeftWoReverse") {
      it("1000000:1") {
        (1 to 1_000_000).toList.sum shouldBe 1784293664
      }
      it("1000:2") {
        (1 to 1000).toList.foldLeft((b: Int) => b) { (bb: Int => Int, a: Int) =>
          b: Int => bb(a + b)
        } (0) shouldBe 500500
      }
    }
    describe("tree") {
      
    }
  }
}
