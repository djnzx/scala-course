package ninetynine

object P49 {
  val variants = List(0, 1)
  
  def gray(n: Int): List[List[Int]] = n match {
    case 0 => List(Nil)
    case n => variants.flatMap(d => gray(n - 1).map(l => d :: l))
  }
  
}

class P49Spec extends NNSpec {
  import P49._
  
  it("1") {
    Seq(
      0 -> List(""),
      1 -> List("0", "1"),
      2 -> List("00", "01", "10", "11"),
      3 -> List("000", "001", "010", "011", "100", "101", "110", "111"),
    )
      .foreach { case (in, out) => 
        gray(in).map(_.mkString).shouldEqual(out)
      }
  }
  
}
