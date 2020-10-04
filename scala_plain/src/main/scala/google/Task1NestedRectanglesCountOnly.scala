package google

object Task1NestedRectanglesCountOnly extends App{
  import Task1NestedRectangles._
  
  def count(rs: IndexedSeq[R]) = {
    val dp = Array.ofDim[Int](rs.length)
    
    rs.indices.foreach { i => // element trying to fit
      rs.indices
        .filter(j => j != i)
        .foreach { j => // fit to all
          if (gt(rs(i), rs(j))) dp(i) += 1
        }
    }
    
    (dp, dp.max+1)
  }
  val rr = IndexedSeq(
    (100,100),
    (20,90),
    (30,80),
    (40,70),
    (50,60),
    (60,50),
    (70,40),
    (80,30),
    (90,20),
  )
  println(rr)
  count(rr.toIndexedSeq) match {
    case (seq, max) => println(seq.mkString("A[", ", ", "]"), max)
  }
}
