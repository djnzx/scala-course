package ninetynine

import tools.spec.ASpec

/**
  * Extract a given number of randomly selected elements from a list
  */
object P23 {
  import P20.extractAt
  import P22.unfoldRight
  import scala.util.Random
  
  def randTo(n: Int) = Random.nextInt(n)
  
  def extractNrandom[A](n: Int, xs: List[A]): List[A] =
    unfoldRight((n, xs.length, xs)) { case (n, len, xs) => (n, len) match {
      case (0, _) => None
      case (_, 0) => throw new NoSuchElementException 
      case (_, _) => extractAt(randTo(len), xs) match { 
        case (xs, item) => Some(((n - 1, len - 1, xs), item))
      }
    }}
  
}

class P23Spec extends ASpec {
  import P23._
  
  it("normal") {
    val data: List[Char] = "QWERTYUIASDFGHJK".toList
    val items5: List[Char] = extractNrandom(5, data)
    items5.length shouldEqual 5
    data should contain allElementsOf items5
  }
  
  it("corner") {
    val data = "ABC".toList // len 3
    val num = 4
    a[NoSuchElementException] should be thrownBy extractNrandom(num, data)
  }
}