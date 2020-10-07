package ninetynine

import tools.spec.ASpec
import scala.annotation.tailrec

/**
  * Duplicate the elements of a list
  */
object P14R {
  
  def duplicate(xs: List[Char]): List[Char] = xs match {
    case Nil    => Nil
    case h :: t => h :: h :: duplicate(t)
  }

}

object P14TR {

  @tailrec
  private def duplicate(xs: List[Char], acc: List[Char]): List[Char] = xs match {
    case Nil    => acc
    case h :: t => duplicate(t, h :: h :: acc)
  }

  def duplicate(xs: List[Char]): List[Char] = duplicate(xs, Nil) reverse

}

class P14Spec extends ASpec {

  it("1") {
    val impls = Vector(
      P14R.duplicate _,
      P14TR.duplicate _,
    )

    val data = Vector(
      "" -> "",
      "A" -> "AA",
      "AB" -> "AABB",
    )

    for {
      impl <- impls
      (in, out) <- data
    } impl(in.toList).mkString shouldEqual out

  }
}
