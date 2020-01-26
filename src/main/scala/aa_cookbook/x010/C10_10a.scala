package aa_cookbook.x010

object C10_10a extends App {
  val fruits = List("apple", "banana", "orange")

  val rangeExact = 1 to fruits.size
  val rangeExact213 = LazyList from 1
  val rangeSmall = 1 to 2
  val rangeBig = 1 to 4

  val z0: Seq[(String, Int)] = fruits zip rangeExact    // 3 zip 3        => 3 same -> same
  val zA: Seq[(String, Int)] = fruits zip rangeExact213 // 3 zip INFINITY => 3 takes the minimal length
  val z1: Seq[(String, Int)] = fruits zip rangeSmall    // 3 zip 2        => 2 shortest wins
  val z2: Seq[(String, Int)] = fruits zip rangeBig      // 3 zip 4        => 3 shortest wins

  println(z0)
  println(zA)
  println(z1)
  println(z2)
}
