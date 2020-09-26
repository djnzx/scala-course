package ninetynine

/**
  * Decode a run-length encoded list
  */
object P12BR {
  
  def unpackOne(c: Char, n: Int): List[Char] = List.fill(n)(c)

  def unpack(xs: List[Any]): List[Char] = xs match {
    case (c: Char) :: t         => c :: unpack(t)
    case (n: Int, c: Char) :: t => unpackOne(c, n) ::: unpack(t)
    case _                      => Nil
  }

}

class P12BRSpec extends NNSpec {
  import P12BR._

  it("1") {
    Vector(
      List() -> "",
      List('A') -> "A",
      List((2,'A')) -> "AA",
      List((2,'A'), 'B') -> "AAB",
      List((2,'A'), (2,'B')) -> "AABB",
      List((2,'A'), (2,'B'), 'C') -> "AABBC",
      List((2,'A'), (2,'B'), 'C', (3, 'D')) -> "AABBCDDD",
    )
      .foreach { case (in, out) =>
        unpack(in).mkString shouldEqual out
      }
  }

} 
