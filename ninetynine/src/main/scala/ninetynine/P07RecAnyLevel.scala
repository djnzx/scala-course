package ninetynine

/**
  * Flatten a nested list structure
  * 
  * recursive implementation
  * flatten any number of levels
  */
object P07RecAnyLevel {
  
  def flatten(xs: List[Any]): List[Any] = xs match {
    case Nil => Nil
    case h::t => h match {
      case n: Int       => n :: flatten(t)
      case l: List[Any] => flatten(l) ++ flatten(t)
    }
  }

}

class P07RecAnyLevelSpec extends NNSpec {
  import P07RecAnyLevel._
  
  it("1") {
    flatten(List(List(1, List(1)), 2, List(3, List(5, List(8))))) shouldEqual List(1,1,2,3,5,8)
  }
  
}