package google2

object Alphabetize extends App {
//  GooZgleX Mail
//    GaeZgilX Mloo

  // O(1)
  def capitalIndexes(s: String): Set[Int] = s.indices.filter(i => s(i).isUpper || s(i) == ' ').toSet

  // 2 O(N) + O(N + NlogN)
  // O(NlogN)
  def alphabetize(origin: String): String = {
    // O(N)
    // A
    val uppercaseIndexes: Set[Int] = capitalIndexes(origin) // 0, 3, 7
    // O(N+N*logN)
    // B (A + B = N)
    val sortedLowercaseChars: String = origin.filter(c => c.isLower).sorted // aegil
    // O(N)
    // N
    def reconstruct(result: String, used: Int): String = {
      val index = result.length
      if (index == origin.length) result
      else if (uppercaseIndexes.contains(index)) reconstruct(result + origin(index), used)
      else reconstruct(result + sortedLowercaseChars(used), used + 1)
    }

    reconstruct("", 0)
  }

  val s = "GooZgleX Mail"
  pprint.pprintln(s)
  pprint.pprintln(alphabetize(s))

}
