package fp_red.red14

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

/**
  * shouldBe vs shouldEqual
  * https://stackoverflow.com/questions/43923312/whats-the-difference-between-shouldbe-vs-shouldequal-in-scala 
  */
class STArraySpec extends AnyFunSpec with Matchers {
  describe("STArray") {
    val src = List(1,2,3)
    val map = Map(0 -> "a", 1 -> "bb", 2 -> "ccc")
    
    it("from") {
      ST.runST(new RunnableST[List[Int]] {
        override def apply[S]: ST[S, List[Int]] = for {
          sta <- STArray.fromList(src)
          a <- sta.freeze
        } yield a
      }) shouldBe List(1,2,3)
    }
    
    it("read") {
      ST.runST(new RunnableST[Int] {
        override def apply[S]: ST[S, Int] = for {
          sta <- STArray.fromList(src)
          a <- sta.read(0)
        } yield a
      }) shouldBe 1
    }

    it("write") {
      ST.runST(new RunnableST[List[Int]] {
        override def apply[S]: ST[S, List[Int]] = for {
          sta <- STArray.fromList(src)
          _ <- sta.write(2, 99)
          a <- sta.freeze
        } yield a
      }) shouldBe List(1,2,99)
    }
    
    it("fill") {
      ST.runST(new RunnableST[List[String]] {
        override def apply[S]: ST[S, List[String]] = for {
          sta <- STArray(3, "")
          _   <- sta.fill(map)
          a   <- sta.freeze
        } yield a
      }) shouldBe List("a", "bb", "ccc")
    } 

  }

}
