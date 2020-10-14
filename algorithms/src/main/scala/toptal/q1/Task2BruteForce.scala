package toptal.q1

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object Task2BruteForce {
  import Task2.pack
  
  def lenT(t: (Char, Int)): Int = t match {
    case (_, 1) => 1
    case (_, n) => n.toString.length + 1
  }
  
  def len(data: List[(Char, Int)]): Int =
    data.foldLeft(0) { (acc, t) => acc + lenT(t)}
  
  def solution(s0: String, k: Int) = 
    (0 to s0.length-k)
      .map { i => s0.substring(0, i) ++ s0.substring(i + k) }
      .map { s => pack(s.toList) }
      .map ( len )
      .min
        
}
class Task2Spec extends AnyFunSpec with Matchers {
  import Task2BruteForce._
  
  it("1") {
    Seq(
      "ABBBCCDDCCC" -> 5,
      "ABCDDDEFG" -> 5,
    )
      .foreach { case (in, out) => solution(in, 3) shouldEqual out }
  }
}

