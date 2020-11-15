package diwo.test

import tools.spec.ASpec

class CombinatoricsSpec extends ASpec  {
  
  import diwo.Combinatorics._
  
  it("binomial C(n,k)") {
    val data = Seq(
      (10, 5) -> 252,
      (5, 2) -> 10
    )
    for {
      ((n, k), c) <- data
    } binomialC(n, k) shouldEqual c
  }
  
  it("variants") {
    
    def mapper(t: ((String, Int), Seq[String])) = t match {
      case ((s, c), out) => (s.toList, c) -> out.map(_.toSet)
    } 
    
    val equals = Seq(
      ("abcd", 3) -> Seq("abc", "abd", "bcd", "acd"),
      ("asdf", 2) -> Seq("as", "da", "af", "sd", "fs", "df")
    ).map(mapper)
    
    val contains = Seq(
      ("qwertyu", 3) -> Seq("wer", "qru"),
      ("zxcvbnm", 5) -> Seq("xcvbm")
    ).map(mapper)
    
    for {
      ((s, n), out) <- equals
    } allCombN(n, s).map(_.toSet) should contain theSameElementsAs out

    for {
      ((s, n), out) <- contains
    } {
      val all = allCombN(n, s).map(_.toSet)
      all should contain allElementsOf out
      all.length shouldEqual binomialC(s.size, n)
    }
    
  }
}
