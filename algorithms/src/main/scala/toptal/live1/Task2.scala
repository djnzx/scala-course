package toptal.live1

import tools.spec.ASpec

/**
  * Task Description
  * A precedence rule is given as "P>E", 
  * which means that letter "P" is followed directly by the letter "E". 
  * Write a function, given an array of precedence rules,
  * that finds the word represented by the given rules.
  *
  * Note: Each represented word contains a set of unique characters, 
  * i.e. the word does not contain duplicate letters.
  *
  * Examples:
  * findWord(["P>E","E>R","R>U"]) // PERU
  * findWord(["I>N","A>I","P>A","S>P"]) // SPAIN
  */
object Task2 {
  
  def findWord(in: Seq[String]): String = {
    
    val m = in.map { case s"$a>$b" => (a(0), b(0)) }.toMap

    /** find the start letter: KEY which DOESN'T appear as a VALUE */
    def findStart(x: Char = m.head._1): Char =
      m.find { case (_, v) => v == x } match {
        case None         => x
        case Some((k, _)) => findStart(k)
      }

    /** construct the word from the given start letter (map traverse)*/
    def makeWord(c: Char, word: List[Char] = Nil): List[Char] =
      m.get(c) match {
        case None    => c::word
        case Some(l) => makeWord(l, c::word)
      }
    
    makeWord(findStart()).reverse.mkString
  }
  
}

class Task2Spec extends ASpec {
  import Task2._
  
  it("1") {
    val data = Seq(
      Seq("P>E","E>R","R>U") -> "PERU",
      Seq("I>N","A>I","P>A","S>P") -> "SPAIN",
      Seq("I>V","K>Y","Y>I") -> "KYIV"
    )
    
    runAllD(data, findWord)
  }
  
}
