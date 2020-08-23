package composition

/**
  * Initial Object
  * Terminal Object
  */
object CatTh041 {
  
  /** Kleisli category, embellishment */
  def identity[A](a: A): (A, String) = (a, "")
  def arrow[A, B](a: A): (B, String) = ???

  /** singleton set - set of 1 elements = Unit */
  def unit_a[A](a: A): Unit = ()
  def true_a[A](a: A): Boolean = true
  
  /** Unit is a terminal object for all arrows */
  def absurd[A](x: Unit): A = ???
}
