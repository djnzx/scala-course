package ndays

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

object NDays {

  // @formatter:off
  def nDays1(n: Int): Int = {
                       //  1  2  3  4  5  6  7  8  9 10 11 12                  
    val n1  = n - 1    //  0  1  2  3  4  5  6  7  8  9 10 11
    val m7  = n1 % 7   //  0  1  2  3  4  5  6  0  1  2  3  4
    val m72 = m7 % 2   //  0  1  0  1  0  1  0  0  1  0  1  0
    val a = 13 - n     // 12 11 10  9  8  7  6  5  4  3  2  1
    
    val d = a % 11     //  1  0 10  9  8  7  6  5  4  3  2  1
    val e = 11 - d     // 10 11  1  2  3  4  5  6  7  8  9 10
    val f = e / 11     //  0  1  0  0  0  0  0  0  0  0  0  0
    
    31 - m72 - f * 2
  }
  
  def nDays1a(n: Int) = 
    31 - (n - 1) % 7 % 2 - ((11 - (13 - n) % 11) / 11) * 2
    
  def nDays2(n: Int): Int = {
                       //  1  2  3  4  5  6  7  8  9 10 11 12                  
    val n1  = n - 1    //  0  1  2  3  4  5  6  7  8  9 10 11
    val m7  = n1 % 7   //  0  1  2  3  4  5  6  0  1  2  3  4
    val m72 = m7 % 2   //  0  1  0  1  0  1  0  0  1  0  1  0
    val a = 13 - n     // 12 11 10  9  8  7  6  5  4  3  2  1
    
    val b = a / 11     //  1  1  0  0  0  0  0  0  0  0  0  0
    val c = a / 12     //  1  0  0  0  0  0  0  0  0  0  0  0
    val f = b ^ c      //  0  1  0  0  0  0  0  0  0  0  0  0
    
    31 - m72 - f * 2
  }
  // @formatter:on

  def nDays2a(n: Int): Int = {
    val a = 13 - n
    31 - (n - 1) % 7 % 2 - (a / 11 ^ a / 12) * 2
  }

  def nDays3(n: Int): Int = {
    30 + (n % 7 % 2) + (n % 8 / 7) - ((11 - n) / 9) * 2 * (n - 1)
  }
}

class NDaysSpec extends AnyFunSpec with Matchers {
  it("nDays") {
    import NDays._
    val data = Seq(
      1 -> 31,
      2 -> 28,
      3 -> 31,
      4 -> 30,
      5 -> 31,
      6 -> 30,
      7 -> 31,
      8 -> 31,
      9 -> 30,
      10 -> 31,
      11 -> 30,
      12 -> 31,
    )
    for {
      (in, out) <- data
    } nDays1a(in) shouldEqual out
  }
}
