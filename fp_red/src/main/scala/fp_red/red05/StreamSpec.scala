package fp_red.red05

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class StreamSpec extends AnyFunSpec with Matchers {
  describe("Stream") {
    import Stream._

    import scala.{Stream => _}
    
    it("toList") {
      Stream(1,2,3,4,5).toList shouldBe List(1,2,3,4,5)
    }
    it("toListTR") {
      Stream(1,2,3,4,5).toList_TR shouldBe List(1,2,3,4,5)
    }
    it("toListFR") {
      Stream(1,2,3,4,5).toList_via_fr shouldBe List(1,2,3,4,5)
    }
    it("toListFast") {
      Stream(1,2,3,4,5).toList_fast shouldBe List(1,2,3,4,5)
    }
    
    
    
    it("map") {
      Stream(1,2,3,4,5).map_fr(_+10).toList shouldBe List(11,12,13,14,15)
    }
    describe("headOption") {
      it("1") {
        Stream(1,2,3).headOption_FR shouldBe Some(1)
      }
      it("2") {
        Stream.empty.headOption_FR shouldBe None
      }
    }
    describe("filter") {
      describe("filter_manual") {
        it("1") {
          Stream(1,2,3,4,5).filter(_ < 4).toList_fast shouldBe List(1,2,3)
        }
        it("2") {
          Stream(1,2,3,4,5).filter(_ > 2).toList_fast shouldBe List(3,4,5)
        }
      }
      describe("filter_foldRight") {
        it("1") {
          Stream(1,2,3,4,5).filter_fr(_ < 4).toList_fast shouldBe List(1,2,3)
        }
        it("2") {
          Stream(1,2,3,4,5).filter_fr(_ > 2).toList_fast shouldBe List(3,4,5)
        }
      }
    }
    describe("append") {
      it("1") {
        Stream(1,2,3).append(Stream(4,5,6)).toList_fast shouldBe List(1,2,3,4,5,6)
      }
      it("2") {
        Stream(1,2,3).append(Stream.empty).toList_fast shouldBe List(1,2,3)
      }
      it("3") {
        Stream.empty.append(Stream(1,2,3)).toList_fast shouldBe List(1,2,3)
      }
    }
    describe("append_fr") {
      it("1") {
        Stream(1,2,3).append_fr(Stream(4,5,6)).toList_fast shouldBe List(1,2,3,4,5,6)
      }
      it("2") {
        Stream(1,2,3).append_fr(Stream.empty).toList_fast shouldBe List(1,2,3)
      }
      it("3") {
        Stream.empty.append_fr(Stream(1,2,3)).toList_fast shouldBe List(1,2,3)
      }
    }
    describe("flatMap") {
      it("1") {
        Stream(1,2,3).flatMap { x => Stream(x+10, x+20) }.toList_fast shouldBe List(11,21,12,22,13,23)
      }
    }
    describe("flatMapFR") {
      it("1") {
        Stream(1,2,3).flatMap_fr { x => Stream(x+10, x+20) }.toList_fast shouldBe List(11,21,12,22,13,23)
      }
    }
    describe("ones") {
      it("1") {
        ones.take(3).toList shouldBe List(1,1,1)
      }
    }
    describe("from") {
      it("1") {
        from(5).take(3).toList shouldBe List(5,6,7)
      }
    }
    describe("unfold") {
      it("0") {
        unfold(6) { i => 
          if (i<10) Some((i, i+1))
          else None
        }.toList shouldBe List(6,7,8,9)
      }
    }
    describe("take_via_unfold") {
      it("1") {
        from(10).take_unfold(4).toList shouldBe List(10,11,12,13)
      }
    }
    
    
    
  }
}
