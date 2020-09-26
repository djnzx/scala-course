package ninetynine

/**
  * Lotto: Draw N different random numbers from the set 1..M.
  */
object P24 {
  import P22._
  import P23._

  def lotto(cnt: Int, mx: Int) =
    extractNrandom(cnt, range_func(1, mx))
    
}

class P24Spec extends NNSpec {
  import P24._

  it("normal") {
    val range = 1 to 49
    
    val lottoOutcome = lotto(6, 49)

    lottoOutcome.length shouldEqual 6
    
    for {
      ball <- lottoOutcome 
    } yield range.contains(ball) shouldEqual true
    
  }


}