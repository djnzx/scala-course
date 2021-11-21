package topics.valdef

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class IteratorSpec extends AnyFunSpec with Matchers {

  val data = List(1, 2, 3)

  describe("iterator is mutable!") {

    it("secondary query is always empty") {
      val it0 = data.iterator

      it0.size shouldEqual 3
      it0.size shouldEqual 0
    }

    it("secondary travers produces empty") {
      val it0 = data.iterator

      it0.toList shouldEqual List(1, 2, 3)
      it0.toList shouldEqual List()
    }

  }

}
