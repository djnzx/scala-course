package fps

trait NormalTupledPlayground {
  // normal => tupled
  def f1[A,B,C](a: A, b: B): C
  // tupled expressed via normal
  def f1t_1[A,B,C](ab: (A, B)): C = ab match { case (a, b) => f1(a, b) }
  def f1t_2[A,B,C](ab: (A, B)): C = f1(ab._1, ab._2)
  
  // tupled
  def f2[A,B,C](ab: (A, B)): C
  // normal expressed via tupled
  def f2n[A,B,C](a: A, b: B): C = f2((a, b))
}
