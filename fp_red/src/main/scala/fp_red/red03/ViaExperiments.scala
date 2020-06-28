package fp_red.red03

import scala.collection.immutable.List

object ViaExperiments {
  
  /** fold to the same structure  */
  def mapViaFoldRight[A, B](as: List[A])(f: A => B): List[B] = {
    as.foldRight(List.empty[B]) { (a: A, bs: List[B]) => f(a)::bs }
  }
  
  
  /** fold to value by building composite function and apply to z
    * foldRight                [B]             (z: B)(f: (A, B) => B): B
    */
  def foldViaFoldRightToFunc[A, B](as: List[A])(z: B)(f: (A, B) => B): B = {
    /**                               B => B  */
    val folded: B => B = as.foldRight(identity[B] _) { (a: A, bb: B => B) => 
      b => bb(f(a, b))
    } 
    folded(z)
  }
  /** the same but  */
  def foldViaFoldRightToFunc2[A, B](as: List[A])(z: B)(f: (A, B) => B): B =
    as.foldRight(identity[B] _) { (a, bb) => b => bb(f(a, b)) } (z)

  /** XOR implementation */
  implicit class RichBoolean(a: Boolean) {
    def ^^(b: Boolean): Boolean = {
      val or = a || b
      val and = a && b
      val xor = or && !and
      xor
    }
  }
  def switch(x: Int): Int = x ^ 1
  def switch1(x: Boolean): Boolean = x ^^ true
  def switch3(x: Boolean): Boolean = x ^ true
  def switch2(x: Boolean): Boolean = !x
  
  def varargs1[A](as: A*): Int = as.length
  // they must have different names because of type erasure
  def varargs2[A](as: Seq[A]): Int = as.length
  // or
  def varargs2a[A](as: Seq[A]): Int = varargs1(as: _*)
  
}
