package topics.recursion.fibo

object FiboApp1 extends App {

  def fibImp(n: Int): Int = {
    var i = 0
    var j = 1

    for (_ <- 0 until n) {
      val l = i + j
      i = j
      j = l
    }
    i
  }

  println(fibImp(10))

}
