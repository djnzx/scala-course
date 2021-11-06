package essential

object X195 extends App {

  val z: Seq[Int] = for (
    x <- Seq(-3,-2,-1,0,1,2,3)
    if x > 0    // converted to a withFilter, and we don't need a parentheses
  ) yield x
  // z = 1,2,3

  val zipped: Seq[(Int, Int)] = Seq(1,2,3) zip Seq(2,3,4)
  val y1: Seq[Int] = zipped map (t => t._1 * t._2)         // 2,6,12
  val y2: Seq[Int] = for { c <- zipped} yield c._1 * c._2 // 2,6,12

  val z1: Seq[(Int, Int)] = for(x <- Seq(1, 2, 3) zipWithIndex) yield x         // from 0
  val z2: Seq[(Int, Int)] = for(x <- Seq(1, 2, 3) zip(LazyList from 1)) yield x // from 1
  val z3: Seq[(Int, Int)] = for(x <- Seq(1, 2, 3) zip(Stream from 1)) yield x   // from 1

  val r4: Seq[Int] = for ((a,b) <- zipped) yield a + b // List(3, 5, 7)

  for {
    x <- Seq(1, 2, 3)
    x2 = x * x
    y <- Seq(4, 5, 6)
  } yield x2 * y
}
