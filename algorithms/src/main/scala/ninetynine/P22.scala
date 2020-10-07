package ninetynine

import tools.spec.ASpec

/**
  * Create a list containing all integers within a given range
  */
object P22 {
  def range_builtin(mn: Int, mx: Int): List[Int] = (mn to mx).toList

  def unfoldRight[S, A](s0: S)(f: S => Option[(S, A)]): List[A] =
    f(s0) match {
      case Some((s1, v)) => v :: unfoldRight(s1)(f) 
      case None => Nil
    }
  
  def range_func(mn: Int, mx: Int): List[Int] =
    unfoldRight(mn) { s =>
      if (s > mx) None
      else        Some((s + 1, s))
    }
    
  def range_r(mn: Int, mx: Int): List[Int] =
    if (mn > mx) Nil
    else mn :: range_r(mn + 1, mx)

  private def range_tr(mn: Int, mx: Int, acc: List[Int]): List[Int] = 
    if (mn > mx) acc reverse
    else range_tr(mn + 1, mx, mn :: acc)

  def range_tr(mn: Int, mx: Int): List[Int] = range_tr(mn, mx, Nil)
}

class P22Spec extends ASpec {
  import P22._
  
  it("1") {
    val mn = 4
    val mx = 9
    val impls = Seq(
      range_func _,
      range_r _,
      range_tr _,
    )
    val exp = range_builtin(mn, mx)
    for {
      impl <-impls
    } impl(mn, mx) shouldEqual exp
  }
}