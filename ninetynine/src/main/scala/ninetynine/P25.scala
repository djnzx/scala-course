package ninetynine

/**
  * Generate a random permutation of the elements of a list
  */
object P25 {
  import P23.extractNrandom
  
  def permutate[A](xs: List[A]) =
    extractNrandom(xs.length, xs)

}

class P25Spec extends NNSpec {
  import P25._
  
  it("1") {
    val MAX = 20
    val range = 1 to MAX
    
    val permutation = permutate(range toList)
    
    permutation.length shouldEqual range.length
    
    for {
      item <- permutation
    } yield range.contains(item) shouldEqual true
    
    println(range toList)
    println(permutation)
  }
}