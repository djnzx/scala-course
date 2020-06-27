package fp_red.red02

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class Red02Spec extends AnyFunSpec with Matchers {
  
  describe("chapter2") {
    
    import Chapter2._
    
    describe("abs") {
      it("1") {
        abs(5) shouldBe 5
      }
      it("2") {
        abs(-5) shouldBe 5
      }
      it("3") {
        abs(0) shouldBe 0
      }
    }
    describe("formatAbs") {
      it("1") {
        formatAbs2(5) shouldBe "The absolute value of 5 is 5"
      }
      it("2") {
        formatAbs2(-5) shouldBe "The absolute value of -5 is 5"
      }
      it("3") {
        formatAbs2(0) shouldBe "The absolute value of 0 is 0"
      }
    }
    describe("recursion") {
      describe("factorial loop") {
        it("1") {
          factorialLoop(0) shouldBe 1
        }
        it("2") {
          factorialLoop(1) shouldBe 1
        }
        it("3") {
          factorialLoop(2) shouldBe 2
        }
        it("5") {
          factorialLoop(5) shouldBe 120
        }
        it("6") {
          factorialLoop(-6) shouldBe 1
        }
      }
      describe("factorial recursion") {
        it("1") {
          factorial(0) shouldBe 1
        }
        it("2") {
          factorial(1) shouldBe 1
        }
        it("3") {
          factorial(2) shouldBe 2
        }
        it("5") {
          factorial(5) shouldBe 120
        }
        it("6") {
          factorial(-6) shouldBe 1
        }
      }
      describe("fibo head recursion") {
        it("0") {
          the [RuntimeException] thrownBy fibHR(0) should have message "fibo isn't defined on numbers < 1"
        }
        it("1") {
          fibHR(1) shouldBe 1
        }
        it("2") {
          fibHR(2) shouldBe 1
        }
        it("3") {
          fibHR(3) shouldBe 2
        }
        it("4") {
          fibHR(4) shouldBe 3
        }
        it("5") {
          fibHR(6) shouldBe 8
        }
      }
      describe("fibo tail recursion") {
        it("1") {
          fib(1) shouldBe 1
        }
        it("2") {
          fib(2) shouldBe 1
        }
        it("3") {
          fib(3) shouldBe 2
        }
        it("4") {
          fib(4) shouldBe 3
        }
        it("5") {
          fib(6) shouldBe 8
        }
      }
      

    }
    describe("format generic") {
      it("abs") {
        formatResult("absolute value", -5, abs) shouldBe "The absolute value of -5 is 5."
      }
      it("fact") {
        formatResult("factorial", 5, factorial) shouldBe "The factorial of 5 is 120."
      }
      it("fibo") {
        formatResult("fibonacci", 7, fib) shouldBe "The fibonacci of 7 is 13."
      }
      it("inc") {
        formatResult("inc", 7, _+1) shouldBe "The inc of 7 is 8."
      }
    }
    describe("polymorphic functions") {
      import PolymorphicFunctions._
      
      describe("binary search") {
        it("found:1") {
          binarySearch[Int](Array(1,2,3,4,5), 4, _ > _) shouldBe 3
        }
        it("found:2") {
          binarySearch[Int](Array(10,20,30,40,50), 40, _ > _) shouldBe 3
        }
        it("notfound:1") {
          binarySearch[Int](Array(1,2,10,20), 5, _ > _) shouldBe -2
        }
        it("notfound:2") {
          binarySearch[Int](Array(1,2,10,20), 55, _ > _) shouldBe -4
        }
      }
      describe("isSorted") {
        it("sorted:1") {
          isSorted[Int](Array(), _ > _) shouldBe true
        }
        it("sorted:2") {
          isSorted[Int](Array(1,2,3,4), _ > _) shouldBe true
        }
        it("unsorted:1") {
          isSorted[Int](Array(5,1,2,3,4,5), _ > _) shouldBe false
        }
        it("unsorted:2") {
          isSorted[Int](Array(1,2,3,4,1), _ > _) shouldBe false
        }
      }
    }
  }
}
