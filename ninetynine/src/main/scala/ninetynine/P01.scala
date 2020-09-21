package ninetynine

import scala.annotation.tailrec

/**
  * Find the last element of a list
  */
object P01 {
  
  @tailrec
  def last[A](as: List[A]): A = as match {
    case Nil      => throw new NoSuchElementException
    case h :: Nil => h
    case _ :: t   => last(t)
  }
  
}

class P01Spec extends NNSpec {
  import P01._
  
  it("should find the last element") {
    val data = List(1, 1, 2, 3, 5, 8)
    last(data) shouldEqual 8
  }
  
  it("should throw an Exception") {
    val empty = List.empty[Int]
    a[NoSuchElementException] should be thrownBy last(empty)
  }
  
}
