package fp_red.red09.playground

trait Playground {
  
  def fabc[A,B,C](a: A, b: B): C
  def fabct[A,B,C](t: (A, B)): C = t match { case (a, b) => fabc(a, b) }
  def fabc2[A,B,C](a: A, b: B): C = fabct((a,b))

  fabc(1,2)
  fabct((1,2))

}
