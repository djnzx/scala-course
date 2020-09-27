package ninetynine

/**
  * A list of Goldbach compositions
  */
object P41 {
  import P40._
  
  def goldbachList(mn: Int, mx: Int, minToPass: Int): Seq[(Int, (Int, Int))] = 
    goldbachList(mn to mx, minToPass)

  def goldbachList(range: Range, minToPass: Int = 1): Seq[(Int, (Int, Int))] =
    range
      .filter(_ > 2)
      .filter(_ % 2 == 0)
      .flatMap(n => goldbach(n) match {
        case Some(r) => Some((n,r)) 
        case _ => None
      })
      .filter { case (_,(a,_)) => a >= minToPass }
  
  implicit class PrettySumFmt(private val seq: Seq[(Int, (Int, Int))]) extends AnyVal {
    def pretty = seq map { case (n,(a,b)) => s"$n = $a + $b" }
  }

}

class P41Spec extends NNSpec {
  import P41._
  
  it("1") {
    goldbachList(9, 20, 1)
      .pretty
      .foreach(println)
  }

  it("2") {
    goldbachList(1 to 2000, 50)
      .pretty
      .foreach(println)
  }
}
