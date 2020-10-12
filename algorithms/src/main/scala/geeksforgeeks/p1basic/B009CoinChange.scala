package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/coin-change-dp-7/
  */
object B009CoinChange {
  /**
    * plain (non-DP)
    * recursive
    * exponential
    */
  def count(coins: Set[Int], sum: Int): Int = {
    if (sum == 0) return 1
    if (sum < 0) return 0
    if (coins.isEmpty) return 0
    
    val m = coins.head
    count(coins - m, sum    ) +
    count(coins,     sum - m) 
  }
}

object B009CoinChangeAllComb {
  /**
    * all possible combinations for the change
    * plain (non-DP)
    * recursive
    * exponential
    */
  def allComb(coins: Set[Int], sum: Int): Seq[Seq[Int]] = {
    if (sum == 0) return Seq(Seq.empty[Int])
    if (sum < 0) return Seq.empty
    if (coins.isEmpty) return Seq.empty

    val coin = coins.head
    
    allComb(coins - coin, sum) ++
    allComb(coins, sum - coin).map(coin +: _)
  }  
}

object B009CoinChangeDP {
  /**
    * plain (non-DP)
    * recursive
    * exponential
    */
  def count(coins: Set[Int], sum: Int): Int = {
    // array represents the sums
    val dp = Array.ofDim[Int](sum + 1)
    dp(0) = 1
    coins.foreach { coin =>
      println(s"coin:$coin")
      (coin to sum).foreach { j =>
        dp(j) += dp(j-coin)
//        println(dp.mkString("Ways:", " ", ""))
      }
      println(dp.mkString("Ways:", " ", ""))
//      println()
    }
    dp.last
  }
}

class B009CoinChange extends ASpec {
  
  it("1") {
    import B009CoinChangeDP._
    val data = Seq(
      (Set(1,2,3),   4)  -> 4,
      (Set(2,3,5,6), 10) -> 5,
    )
    
    runAllD(data, (count _).tupled)
  }
  
  it("2") {
    import B009CoinChangeAllComb._
    
    val data = Seq(
      (Set(1,2,3), 4) -> Seq(
        Seq(1,1,1,1),
        Seq(1,1,2),
        Seq(2,2),
        Seq(1,3),
      )
    )
    
    for {
      ((seq, sum), r) <- data
    } allComb(seq, sum) should contain theSameElementsAs r
    
  }
  
  
}

