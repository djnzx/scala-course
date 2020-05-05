package hackerrank.d200319_04

/**
  * https://www.hackerrank.com/challenges/climbing-the-leaderboard/problem
  */
object ClimbingLeaderboardScala extends App {

  case class Score(x: Int)
  val scmp: java.util.Comparator[Score] = (s1: Score, s2: Score) => s2.x - s1.x

  def rank(scores: Array[Score], alice: Score): Int = {
    val idx = java.util.Arrays.binarySearch(scores, alice, scmp)
    if (idx < 0) idx.abs else idx+1
  }

  def climbingLeaderboard(scores: Array[Int], alice: Array[Int]): Array[Int] = {
    val distinct = scores.distinct.map(Score)
    alice.map { a => rank(distinct, Score(a)) }
  }

  val scores = Array(100,100,50,40,40,20,10)
  val alice = Array(5,25,50,120)
  printa(scores)
  printa(alice)
  printa(climbingLeaderboard(scores, alice))
  def printa(a: Array[Int]) = println(java.util.Arrays.toString(a))
}
