package ninetynine

import scala.annotation.tailrec

/**
  * Find the last but one element of a list
  */
object P02 {
  
  @tailrec
  def penultimate[A](as: List[A]): A = as match {
    case Nil |
         _ :: Nil      => throw new NoSuchElementException
    case x :: _ :: Nil => x
    case _ :: t        => penultimate(t)
  }

}

class P02Spec extends NNSpec {
  import P02._
  
  it("should throw an exception on empty list") {
    a[NoSuchElementException] should be thrownBy penultimate(List.empty[Int])
  }
  
  it("should throw an exception on list size of 1") {
    a[NoSuchElementException] should be thrownBy penultimate(List(13))
  }
  
  it("should return penultimate element") {
    penultimate(List(1,1,2,3,5,8)) shouldEqual 5
  }
  
}