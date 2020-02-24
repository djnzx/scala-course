package book_red

object Red007step1 extends App {

  type Par[A]

  object Par {
    // it will create a UNIT of PARALLELISM based on value
    def unit[A](a: A): Par[A] = ???
    // it will extract a VALUE
    def get[A](pa: Par[A]): A = ???
  }

  // sum, divide and conquer approach
  def sum(xs: IndexedSeq[Int]): Int = xs.length match {
    case 0 => 0
    case 1 => xs.head
    case _ => {
      // split as usual
      val (l, r) = xs.splitAt(xs.length/2)
      // create units
      val lr: Par[Int] = Par.unit(sum(l))
      // create units
      val rr: Par[Int] = Par.unit(sum(r))
      // extracting value. waiting !!!
      val sumL: Int = Par.get(lr)
      // extracting value. waiting !!!
      val sumR: Int = Par.get(rr)
      // sum the partial results
      sumL + sumR
    }
  }
  
  val data = IndexedSeq(1,2,3,4,5)
  val res = sum(data)
  println(s"sum = $res")
  
}
