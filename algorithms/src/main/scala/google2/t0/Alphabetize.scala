package google2.t0

/**   - GooZgleX Mail
  *   - GaeZgilX Mloo
  */
object Alphabetize extends App {

  // calculate indexes of lowercase letters
  def calculateUppercaseIndexes(s: String): Set[Int] =
    s.indices.filter(i => s(i).isUpper || s(i) == ' ').toSet

  def alphabetize(origin: String): String = {
    val uppercaseIndexes: Set[Int] = calculateUppercaseIndexes(origin)
    val lowercaseSorted: String = origin.filter(_.isLower).sorted

    def reconstruct(outcome: List[Char], index: Int, used: Int): String = {
      if (index == origin.length) outcome.reverse.mkString
      else if (uppercaseIndexes.contains(index)) reconstruct(origin(index) :: outcome, index + 1, used)
      else reconstruct(lowercaseSorted(used) :: outcome, index + 1, used + 1)
    }

    reconstruct(Nil, 0, 0)
  }

  val s = "GooZgleX Mail"
  pprint.pprintln(s)
  pprint.pprintln(alphabetize(s))

}
