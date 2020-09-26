package ninetynine

/**
  * Generate the combinations of K distinct objects chosen from the N elements of a list
  * 
  *     n!
  * -----------    
  * k! * (n-k)!
  * 
  * 3 of 4 = 4 
  * 3 of 5 = 10
  * 
  *     5!
  * ----------- = 10
  * 3! * (5-3)!
  * 
  */
object P26 {
  type L[A] = List[A]
  type LL[A] = List[List[A]]
  
  def tails[A](la: List[A])(f: L[A] => LL[A]): LL[A] = la match {
    case Nil => Nil
    case _ :: t => f(la) ::: tails(t)(f) 
  }
  
  def combinations[A](n: Int, la: L[A]): LL[A] = n match {
    case 0 => List(Nil)
    case _ => tails(la) { case h :: t => 
      combinations(n - 1, t) map(h :: _)
    }
  }
}

class P26Spec extends NNSpec {
  import P26._
  
  it("1") {
    val data = Seq(
      (1,"abc") -> Seq("a", "b", "c"),
      (2,"abc") -> Seq("ab", "ac", "bc"),
      (3,"abc") -> Seq("abc"),
      (3,"abcd") -> Seq("abc","abd","acd","bcd"),
      (4,"abcde") -> Seq("abcd","abce","abde","acde","bcde"),
      (3,"abcde") -> Seq("abc","abd","abe","acd","ace","ade","bcd","bce","bde","cde"),
    )

    for {
      ((n, in), out) <- data
    } combinations(n, in.toList) shouldEqual out.map(_.toList)
  }
  
}