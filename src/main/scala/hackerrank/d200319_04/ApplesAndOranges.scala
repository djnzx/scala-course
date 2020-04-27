package hackerrank.d200319_04

object ApplesAndOranges {
  def countApplesAndOranges5(s: Int, t: Int, a: Int, b: Int, apples: Array[Int], oranges: Array[Int]): Unit = {
    type FII = Int => Int
    def ip(p: Int): Boolean = p >= s && p <= t
    def plus(x: Int): FII = _ + x
    def count(ints: Array[Int], f: FII) = ints map f count ip
    val ac = count(apples, plus(a))
    val oc = count(oranges, plus(b))
    println(s"$ac\n$oc")
  }
}
