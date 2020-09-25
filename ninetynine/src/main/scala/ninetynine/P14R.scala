package ninetynine

/**
  * Duplicate the elements of a list
  */
object P14R {
  
  def duplicate(xs: List[Char]): List[Char] = xs match {
    case Nil    => Nil
    case h :: t => h :: h :: duplicate(t)
  }

}

class P14RSpec extends NNSpec {
  import P14R._
  
  it("1") {
    Vector(
      "" -> "",
      "A" -> "AA",
      "AB" -> "AABB",
    )
      .foreach { case (in, out) =>
        duplicate(in.toList).mkString shouldEqual out
      }
  }
}
