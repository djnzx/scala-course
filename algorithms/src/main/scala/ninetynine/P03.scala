package ninetynine

import tools.spec.ASpec
import scala.annotation.tailrec

/**
  * Find the Kth element of a list
  * elements are being counted from zero
  */
object P03 {
  
  @tailrec
  def nth[A](n: Int, as: List[A]): A = (n, as) match {
    case (_, Nil)    => throw new NoSuchElementException
    case (0, h :: _) => h
    case (n, _ :: t) => nth(n - 1, t)
  }
  
}

class P03Spec extends ASpec {
  import P03._

  it("should throw an exception on empty list") {
    a[NoSuchElementException] should be thrownBy nth(0, List.empty[Int])
  }

  it("should throw an exception on N > list.length") {
    a[NoSuchElementException] should be thrownBy nth(5, List(1,2,3,4,5))
  }

  it("should provide an K-th element of the list N <= list length") {
    nth(3, List(10,20,30,40)) shouldEqual 40
    nth(2, List(10,20,30,40)) shouldEqual 30
  }
}