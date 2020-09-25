package ninetynine

import scala.annotation.tailrec

/**
  * Reverse a list
  */
object P05 {
  
  def reverse[A](as: List[A]): List[A] = {
    
    @tailrec
    def reverse(as: List[A], acc: List[A]): List[A] = as match {
      case Nil  => acc;
      case h::t => reverse(t, h :: acc)
    }
    
    reverse(as, Nil)
  }

}

class P05Spec extends NNSpec {
  import P05._
  
  it("should return empty list for empty list") {
    reverse(List.empty) shouldEqual List.empty
  }
  
  it("should return the same list") {
    reverse(List(42)) shouldEqual List(42)
  }

  it("should return reversed list") {
    reverse(List(7,13,42)) shouldEqual List(42,13,7)
  }
}
