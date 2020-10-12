package geeksforgeeks.p1basic

import tools.spec.ASpec

object B010FriendsPairing {
  def nWays(n: Int): Int = {
    if (n == 1) return 1
    if (n == 2) return 2

    nWays(n - 1) +          // N-th is alone 
    (n - 1) * nWays(n - 2)  // N-th is paired with every 
  }
}

object B010FriendsPairingCombinations {
  import ninetynine.P26._
  import ninetynine.P27._
  
  def allCombN[A](data: L[A], n: Int): LLL[A] = {
    val N = data.size
    if (N == 0) return List(Nil)
    require(N % n == 0, s"data size ($N) should be divisible by $n!")

    combinations(n, data)                     // 1. all pairs combinations 
      .flatMap { la: L[A] =>                   // 2. take n-th pair
        val a = allCombN(data -- la, n)       // 3. calculate the tail without that pair
        val b = a.map { x: LL[A] => la :: x } // 4. attach the n-th pair from step2 
        b
      }
  }
  
  def allComb12[A](data: L[A]): LLL[A] = {
    val N = data.size
    if (N == 0) return List(Nil)

    (1 to 2)                                              // 1. all combinations by1 and by2
      .map { n => combinations(n, data) }
      .reduce(_:::_)
//      .foldLeft(List(List.empty[A]))(_:::_)             // I have no idea why foldLeft DOESN'T WORK
      .flatMap { la: L[A] =>                               // 2. take n-th group (1/2 - doesn't matter)
        val a = allComb12(data -- la)                     // 3. calculate the tail without taken group
        val b = a.map { x: LL[A] => la :: x }             // 4. attach the taken group from step2 
        b
      }
  }
}

class B010FriendsPairingSpec extends ASpec {
  it("1") {
    import B010FriendsPairing._
    
    val data = Seq(
      1 -> 1,
      2 -> 2,
      3 -> 4,
      4 -> 10
    )
    
    runAllD(data, nWays)
  }
  
  it("2") {
    import B010FriendsPairingCombinations._
    allCombN((1 to 6).toList, 3)
//      .map(_.toSet)
//      .toSet
      .foreach(println)
  }
  
  it("3") {
    import B010FriendsPairingCombinations._
    
    allComb12((1 to 4).toList)
      .map(_.toSet)
      .toSet
      .foreach(println)
  }
  
  
}
