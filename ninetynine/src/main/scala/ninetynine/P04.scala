package ninetynine

import scala.annotation.tailrec

/**
  * Find the number of elements of a list
  */
object P04 {
  
  def length[A](as: List[A]): Int = {
    @tailrec
    def len(as: List[A], acc: Int): Int = as match {
      case Nil  => acc
      case _::t => len(t, acc + 1)
    }
    len(as, 0)
  }

}

class P04Spec extends NNSpec {
  
  it("should return 0 on empty list") {
    P04.length(List.empty[Int]) shouldEqual 0
  }

  it("should return length of non-empty list") {
    P04.length(List(1, 1, 2, 3, 5, 8)) shouldEqual 6
  }
}
