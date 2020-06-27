package fp_red.red01

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class PlaygroundSpec extends AnyFunSpec with Matchers {

  describe("Playground") {
    describe("own types") {
      import NewTypes._

      it("inc") {
        inc(5) shouldBe 6
      }
      it("twice") {
        twice(i => i + 2)(5) shouldBe 9
      }
      it("unit7:1") {
        unit7(3) shouldBe 7
      }
      it("unit7:2") {
        unit7(0) shouldBe 7
      }
    }
    describe("dropWhile") {
      import DropWhile._

      it("1") {
        dropWhile[Int](List.empty, _ < 3) shouldBe List.empty
      }
      it("2") {
        val data = List(1, 2, 3, 4, 5, 6)
        dropWhile[Int](data, _ < 3) shouldBe List(3, 4, 5, 6)
      }
      it("3") {
        val data = List(6, 5, 4, 3, 2, 1)
        dropWhile[Int](data, _ < 3) shouldBe data
      }
    }
    describe("lift") {
      import LiftApproach._

      it("1") {
        val inc: Int => Int = (a: Int) => a + 1
        lift(inc)(Some(5)) shouldBe Some(6)
      }
      it("2") {
        val inc = (s: String) => s + s
        lift(inc)(Some("ABC")) shouldBe Some("ABCABC")
      }
    }
    describe("validation") {
      import Validation._

      val NE = Left("Name is empty")
      val AL = Left("Age < 0")

      it("valid") {
        mkPerson("Alex", 33) shouldBe Right(Person(Name("Alex"), Age(33)))
      }
      it("invalid:1") {
        mkPerson(null, -5) shouldBe NE
      }
      it("invalid:2") {
        mkPerson("", -5) shouldBe NE
      }
      it("invalid:3") {
        mkPerson("Alex", -5) shouldBe AL
      }
    }
    describe("streams") {
      import StreamsIdea._

      describe("construction") {
        describe("empty") {
          it("1") {
            Stream.empty shouldBe Empty
          }
          it("2") {
            Stream.apply() shouldBe Empty
          }
          it("3") {
            Stream() shouldBe Empty
          }
        }
        
        describe("non-empty, headOption") {
          it("1") {
            Empty.headOption shouldBe None
          }
          it("2") {
            Cons(() => 11, () => Empty).headOption shouldBe Some(11)
          }
          it("3") {
            Stream(11).headOption shouldBe Some(11)
          }
          it("4") {
            Stream(22, 33).headOption shouldBe Some(22)
          }
          it("5") {
            Stream.cons(11, Stream.cons(22, Stream.cons(33, Stream.empty))).headOption shouldBe
              Some(11)
          }
          it("6") {
            (Stream(1, 2, 3, 4) match {
              case Cons(h, _) => h()
            }) shouldBe 1
          }
        }
      }
    }
    describe("laziness") {
      import Laziness._
      
      it("should print 'hi' twice") {
        lazyTwice(true, { println("hi"); 1 + 41 }) shouldBe 84
      }
      it("should print 'HI' once") {
        lazyCached(true, { println("HI"); 1 + 41 }) shouldBe 84
      }
    }
  }
}
