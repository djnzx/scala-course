package geeksforgeeks

import scala.collection.mutable

/**
  * https://www.geeksforgeeks.org/water-jug-problem-using-memoization/?ref=rp
  */
object DP_WaterJug extends App {
  
  val visited = mutable.Map.empty[(Int, Int), Boolean]
  
  val jug1 = 4
  val jug2 = 3
  val aim  = 2
  
  def solve(a1: Int, a2: Int): Boolean =
    if ((a1 == 0 && a2 == aim) || (a2 == 0 && a1 == aim)) {
      // termination condition
      println(s"($a1, $a2)")
      true
    }
    else if (visited.contains((a1, a2))) false
    else {
      // continuation
      println(s"($a1, $a2)")
      visited.put((a1,a2), true)
      
      solve(0, a2) || // spill 1
      solve(a1, 0) || // spill 2
      solve(jug1, a2) ||  // fill 1st to the max
      solve(a1, jug2) ||  // fill 2nd to the max
      {
        // 1->2
        if (a1 + a2 <= jug2) 
          solve(0, a1 + a2)         // 1->2 until 1 is empty
        else 
          solve(a1 - (jug2 - a2), jug2) // 1->2 until 2 is full
      } ||
      {
        if (a1 + a2 <= jug1)
          solve(a1 + a2, 0)         // 1<-2 until 2 is empty
        else 
          solve(jug1, a2 - (jug1 - a2)) // 1<-2 until 1 is full
      }
    }


  solve(0,0)
}
