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
  def allComb(n: Int): List[List[List[Int]]] = {
    if (n == 1) return List(List(List(1)))
    if (n == 2) return List(List(List(1), List(2)), List(List(1,2)))

    val alone: List[List[List[Int]]] = List(List(List(n))) ::: allComb(n - 1)
    val paired = allComb(n - 2).map(g => g.map(n :: _))
    alone ::: paired
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
    
    allComb(3).foreach(println)
  }
  
  
}
