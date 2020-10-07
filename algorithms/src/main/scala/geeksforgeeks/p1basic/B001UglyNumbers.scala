package geeksforgeeks.p1basic

import tools.spec.ASpec

/**
  * https://www.geeksforgeeks.org/ugly-numbers/
  */
object B001UglyNumbers {
  import ninetynine.P35._

  /**
    * bruteforce,
    * builds 
    */
  def isUgly(n: Int) =
    (primeFactorsDistinct(n) -- Set(2,3,5)).isEmpty || n == 1

  /**
    * dynamic:
    * we divide, until it's divisible
    * and check for a remainder
    */
  def divBy(n: Int, by: Int): Int = n % by match {
    case 0 => divBy(n / by, by)
    case _ => n
  }   

  def isUglyDP(n: Int) = {
    val n1 = divBy(n, 2)
    val n2 = divBy(n1, 3)
    val n3 = divBy(n2, 5)
    n3 == 1
  }
  
  def nthUgly(n: Int): Int =
    LazyList.from(1).filter(isUglyDP).slice(n - 1, n).head
}

class B001UglyNumbersSpec extends ASpec {
  import B001UglyNumbers._
  it("1") {
    
    val data = Seq(
      7 -> 8,
      10 -> 12,
      15 -> 24,
      150 -> 5832,
    )
    
    runAllD(data, nthUgly _)
  }
  
  it("2") {
    import ninetynine.P31.primes
    
    println(primes.take(20).toList)
  }
}