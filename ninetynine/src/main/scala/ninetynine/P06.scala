package ninetynine

import scala.annotation.tailrec

/**
  * Find out whether a list is a palindrome
  */
object P06 {

  def isPalindrome[A](as: List[A]): Boolean = {
    import P05A.reverseAndLen
    
    @tailrec
    def check(n: Int, a: List[A], b: List[A]): Boolean = (n,a,b) match {
      case (0, _, _) => true
      case (_, ah::at, bh::bt) => if (ah == bh) check(n-1, at, bt) else false
    }
    val (asr, len) = reverseAndLen(as)
    check(len / 2, as, asr)
  }

}

class P06Spec extends NNSpec {
  import P06._
  
  it("should work") {
    val t = List(
      List(1, 2, 3, 3, 2, 1),
      List(1, 2, 5, 2, 1),
      List(1),
      List.empty[Int],
    )

    val f = List(
      List(1, 1, 2, 3, 5, 8),
      List(1, 2, 5, 6, 2, 1)
    )

    (t.map(_->true).toMap ++ f.map(_->false).toMap)
      .forall { case (data, res) =>
        isPalindrome(data) == res
      } shouldEqual true
  }
  
}
