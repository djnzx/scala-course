package fp_red.red03

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class DataStructSpec extends AnyFunSpec with Matchers {
  describe("data structures") {
    describe("list") {
      implicit class ListOps[A](as: List[A]) {
        def append(bs: List[A]): List[A] = List.append(as, bs)
        def startsWith(bs: List[A]): Boolean = List.startsWith(as, bs)
        def hasSubsequence(bs: List[A]): Boolean = List.hasSubsequence(as, bs)
        def zipWith[B](bs: List[B]): List[(A, B)] = List.zipWithTR(as, bs)((_, _))
        def reverse: List[A] = List.reverse(as)
      }
      
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
      describe("zipWith") {
        val a = List()
        val b = List(1, 2, 3)
        val c = List(10, 20, 30, 40)
        val add: (Int, Int) => Int = _ + _
        it("1") {
          a zipWith b shouldBe List()
        }
        it("2") {
          b zipWith c shouldBe List((1, 10), (2, 20), (3, 30))
        }
        it("3") {
          List.zipWithTR(b,c)(add) shouldBe List(11,22,33)
        }
      }
      describe("list startsWith/hasSubsequence") {
        val xs = List(1,2,3)
        val ys = List(4,5,6)
        val zs = List(7,8,9)

        it("1") {
          xs startsWith Nil shouldBe true
        }
        it("2") {
          (xs append ys) startsWith xs shouldBe true
        }
        it("3") {
          xs hasSubsequence Nil shouldBe true
        }
        it("4") {
          (xs append ys) hasSubsequence xs shouldBe true
        }
        it("5") {
          (xs append ys) hasSubsequence ys shouldBe true
        }
        it("6") {
          (xs append ys append zs) hasSubsequence ys shouldBe true
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
      describe("size") {
        it("1") {
          val t: Tree[Int] = Leaf(5)
          Tree.size(t) shouldBe 1
        }
        it("2") {
          val t: Tree[Int] = Branch(Leaf(5), Branch(Leaf(10), Leaf(20)))
          Tree.size(t) shouldBe 5
        }
      }
      describe("max") {
        it("1") {
          val t: Tree[Int] = Branch(Leaf(5), Branch(Leaf(10), Leaf(20)))
          Tree.maximum(t) shouldBe 20
        }
      }
      describe("depth") {
        val t: Tree[Int] = Branch(Leaf(5), Branch(Leaf(10), Branch(Leaf(40), Leaf(50))))
        // this implementation starts from 1
        it("1") {
          Tree.depth2(t) shouldBe 4
        }
        // this implementation starts from 0
        it("2") {
          Tree.depth(t) shouldBe 3
        }
      }
      describe("map") {
        val t: Tree[String] = Branch(Leaf("a"), Branch(Leaf("bb"), Branch(Leaf("ccc"), Leaf("dddd"))))
        val t2: Tree[Int] = Branch(Leaf(1), Branch(Leaf(2), Branch(Leaf(3), Leaf(4))))
        val f: String => Int = _.length
        it("1") {
          Tree.map(t)(f) shouldBe t2
        }
      }
      describe("sizeViaFold") {
        it("1") {
          val t: Tree[Int] = Leaf(5)
          Tree.sizeViaFold2(t) shouldBe 1
        }
        it("2") {
          val t: Tree[Int] = Branch(Leaf(5), Branch(Leaf(10), Leaf(20)))
          Tree.sizeViaFold2(t) shouldBe 5
        }
      }
      describe("maxViaFold") {
        it("1") {
          val t: Tree[Int] = Branch(Leaf(5), Branch(Leaf(10), Leaf(20)))
          Tree.maxViaFold(t) shouldBe 20
        }
      }
      describe("depthViaFold") {
        val t: Tree[Int] = Branch(Leaf(5), Branch(Leaf(10), Branch(Leaf(40), Leaf(50))))
        // this implementation starts from 0
        it("2") {
          Tree.depthViaFold(t) shouldBe 3
        }
      }
      describe("mapViaFold") {
        val t: Tree[String] = Branch(Leaf("a"), Branch(Leaf("bb"), Branch(Leaf("ccc"), Leaf("dddd"))))
        val t2: Tree[Int] = Branch(Leaf(1), Branch(Leaf(2), Branch(Leaf(3), Leaf(4))))
        val f: String => Int = _.length
        it("1") {
          Tree.mapViaFold2(t)(f) shouldBe t2
        }
      }
    }
  }
}
