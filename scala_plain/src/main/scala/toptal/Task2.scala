package toptal

object Task2 {

  def stringToTuples(s: String): List[(Char, Int)] = {
   
    def pack(xs: List[Char], buf: (Char, Int), acc: List[(Char, Int)]): List[(Char, Int)] = (xs, buf) match {
      case (Nil, _)  => acc :+ buf
      case (xh::xt, (ch, cnt)) =>
        if (xh == ch) pack(xt, (ch, cnt + 1), acc)
        else          pack(xt, (xh, 1)      , acc :+ buf)
    }
    
    s match {
      case "" => Nil
      case _ =>
        val h::t = s.toList
        pack(t, (h, 1), Nil)
    }
  }

  def squashK(data: List[(Char, Int)], k:Int): List[(Char, Int)] = ???
  
  def squashDup(data: List[(Char, Int)]): List[(Char, Int)] = ???
  
  def lenT(t: (Char, Int)): Int =
    t._2.toString.length + 1
  
  def len(data: List[(Char, Int)]): Int =
    data.foldLeft(0) { (acc, t) => acc + lenT(t)}
  
  def solution(s: String, k: Int): Int = {
    val sq1 = stringToTuples(s)
    val sq2 = squashK(sq1, k)
    val sq3 = squashDup(sq2)
    len(sq3)
  }  
}
