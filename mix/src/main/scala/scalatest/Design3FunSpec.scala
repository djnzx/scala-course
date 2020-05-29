package scalatest

import org.scalatest.funspec._

/**
  * For teams coming from Ruby's RSpec tool, FunSpec will feel very familiar;
  * More generally, for any team that prefers BDD, FunSpec's nesting and
  * gentle guide to structuring text (with describe and it) provides an excellent
  * general-purpose choice for writing specification-style tests.
  */
class Design3FunSpec extends AnyFunSpec {

  describe("A Set") {
    describe("when empty") {
      it("should have size 0") {
        assert(Set.empty.size == 0)
      }

      it("should produce NoSuchElementException when head is invoked") {
        assertThrows[NoSuchElementException] {
          Set.empty.head
        }
      }
    }
  }
}
