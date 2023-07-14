package composition

import cats.Monoid

object CatTh032 {
  
  var globalLog = ""
  /** extremely bad */
  def negate0(x: Boolean) = {
    globalLog += "not!" 
    !x
  }
  
  /** better but still bad */
  def negate1(x: Boolean, log: String): (Boolean, String) =
    (!x, log + "not!")
  
  /** better */
  def negate2(x: Boolean): (Boolean, String) = (!x, "not!")
  
  /** regular function composition */
  def compose[A, B, C](f: A => B, g: B => C): A => C =
    a => g(f(a))

  /**
    * embellish, we defined a new way of composition
    * associativity => YES
    * identity => YES
    */
  def compose2[A, B, C](f: A => (B, String), g: B => (C, String)): A => (C, String) = {
    a: A =>
      val (b, s1): (B, String) = f(a)
      val (c, s2): (C, String) = g(b)
      (c, s1 + s2)
  }

  /** identity element - "lifter" */
  def identity2[A](a: A): (A, String) = (a, "")
  
  /**
    * and right now,
    * we dont need to fix to the String.
    * it can be any Monoid !
    * Monoid - is a minimal imposition
    * 
    * So, we have a category: Kleisli Arrows
    * 
    * A -> B means:
    * 
    * A => (B, D)
    * B => (C, D)
    * =>
    * A => (C, D)
    * 
    * Monad. Just a way of composition
    * Just additional degree of freedom!
    */
  def compose3[A, B, C, D](f: A => (B, D), g: B => (C, D))(implicit md: Monoid[D]): A => (C, D) = {
    a: A =>
      val (b, d1): (B, D) = f(a)
      val (c, d2): (C, D) = g(b)
      (c, md.combine(d1, d2))
  }
  
  
}
