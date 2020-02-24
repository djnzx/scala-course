package book_red

object Red007step0 extends App {
  
  // sum, divide and conquer approach
  def sum(xs: IndexedSeq[Int]): Int = xs.length match {
    case 0 => 0
    case 1 => xs.head
    case _ => xs.splitAt(xs.length/2) match {
      case (l, r) => sum(l) + sum(r)
    }
  }
  
  val data = IndexedSeq(1,2,3,4,5)
  val res = sum(data)
  println(s"sum = ${res}")
  
}
