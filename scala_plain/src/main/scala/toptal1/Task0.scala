package toptal1

object Task0 {

  def minNonNeg(a: Array[Int]): Int = {
    val as: Array[Int] = a.filter { _ > 0 }.sorted

    def find(idx: Int, min: Int): Int =
      if (idx == as.length || as(idx) > min) min
      else find(idx + 1, as(idx) + 1)

    find(0, 1)
  }
  
}
