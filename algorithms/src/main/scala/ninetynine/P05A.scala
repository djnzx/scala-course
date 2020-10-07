package ninetynine

import tools.spec.ASpec
import scala.annotation.tailrec

/**
  * by doing reverse, we can calculate the length for free
  */
object P05A {
  
  def reverseAndLen[A](as: List[A]): (List[A], Int) = {
    
    @tailrec
    def reverse(as: List[A], acc: (List[A], Int)): (List[A], Int) = (as, acc) match {
      case (Nil, _)                => acc;
      case (h :: t, (list, count)) => reverse(t, (h :: list, count + 1))
    }
    
    reverse(as, (Nil, 0))
  }
  
}

class P05ASpec extends ASpec {
  import P05A._
  
  it("should work 1") {
    reverseAndLen(List.empty[Int]) shouldEqual (List.empty[Int], 0)
  }
  
  it("should work 2") {
    reverseAndLen(List(1, 5)) shouldEqual (List(5, 1), 2)
  }
}
