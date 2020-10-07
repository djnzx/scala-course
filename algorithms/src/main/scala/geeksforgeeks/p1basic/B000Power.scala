package geeksforgeeks.p1basic

import tools.spec.ASpec

object B000Power {
  
  def power(x: Int, y: Int): Int = y match {
    case 0 => 1
    case _ => (1 until y).foldLeft(x)((a, _) => a * x)
  }

}

class B000PowerSpec extends ASpec {
  import B000Power._
  
  it("1") {
    val data = Seq(
      (1, 1) -> 1,
      (1, 0) -> 1,
      (13, 0) -> 1,
      (2, 2) -> 4,
      (2, 3) -> 8,
      (3, 1) -> 3,
      (3, 2) -> 9,
      (3, 3) -> 27,
    )
    
    runAllD(data, (power _).tupled)
  }
  
}