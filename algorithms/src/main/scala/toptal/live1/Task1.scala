package toptal.live1

import tools.spec.ASpec

/**
  * Task Description
  * A vending machine has the following denominations: 1c, 5c, 10c, 25c, 50c, and $1. 
  * Your task is to write a program that will be used in a vending machine to return change.
  * Assume that the vending machine will always want to return the least number of coins or notes. 
  * Devise a function getChange(M, P) where M is how much money was inserted into the machine 
  * and P the price of the item selected, that returns an array of integers representing 
  * the number of each denomination to return. 
  *
  * Example:
  * getChange(5, 0.99) // should return [1,0,0,0,0,4]
  * 
  */
object Task1 {
  
  def getChange(inserted: Double, price: Double) = {
    
    val coins = Vector(1, 5, 10, 25, 50, 100)
    def norm(x: Double) = (x * 100).toInt
    def biggest(amt: Int) = coins.findLast(_ <= amt).get
    val change = Array.ofDim[Int](coins.length)
    
    def process(amt: Int): Unit = if (amt > 0) {
      val coin = biggest(amt)
      val idx = coins.indexOf(coin)
      change(idx) += 1
      process(amt - coin)
    }
    
    process(norm(inserted) - norm(price))
    change
  }
  
}

class Task1Spec extends ASpec {
  import Task1._
  
  it("1") {
    val data: Seq[((Double, Double), Array[Int])] = Seq(
      (5.0,  0.99) -> Array(1, 0, 0, 0, 0, 4),
      (3.14, 1.99) -> Array(0, 1, 1, 0, 0, 1),
      (3.0,  0.01) -> Array(4, 0, 2, 1, 1, 2),
      (4.0,  3.14) -> Array(1, 0, 1, 1, 1, 0),
      (0.45, 0.34) -> Array(1, 0, 1, 0, 0, 0),
    )

    runAllD(data, (getChange _).tupled)
  }
  
}
