package ninetynine

import scala.annotation.tailrec

/**
  * Flatten a nested list structure
  * 
  * tail recursive implementation
  * flatten only one level
  */
object P07TROneLevel {
  
  def flatten(xsa: List[Any]): List[Any] = {
    @tailrec
    def flatten(xs: List[Any], acc: List[Any]): List[Any] = xs match {
      case Nil => acc
      case h::t => h match {
        case n: Int       => flatten(t, acc :+ n)
        case l: List[Any] => flatten(t, acc ++ l)
      }
    }
    
    flatten(xsa, Nil)
  }

}

class P07TROneLevelSpec extends NNSpec {
  import P07TROneLevel._
  
  it("1") {
    val data: List[Any] = List(List(1, 1), 2, List(3, List(5, 8)))
    flatten(data) shouldEqual List(1, 1, 2, 3, List(5, 8))
  }
  
}