package topics.recursion.fibo

object FiboApp2 extends App {

  def fibRec(n: Int): Int = n match {
    case 1 => 1
    case 2 => 1
    case _ => fibRec(n-1) + fibRec(n-2)
  }

  println(fibRec(10))
}
