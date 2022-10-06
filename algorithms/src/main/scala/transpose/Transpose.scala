package transpose

import scala.util.chaining.scalaUtilChainingOps

object Transpose extends App {

  def doPrint(xss: Seq[Seq[Int]]) =
    xss
      .map(_.mkString(" "))
      .mkString("\n")
      .pipe(println)

  val data = List(
    List(1, 2, 3),
    List(4, 5, 6)
  )

  val expected = List(
    List(1, 4),
    List(2, 5),
    List(3, 6)
  )

  doPrint(data)
  println("--")
  doPrint(expected)

  def map2[A, B, C](as: Seq[A], bs: Seq[B])(f: (A, B) => C): Seq[C] = (as zip bs).map(f.tupled)

  def transpose(xss: Seq[Seq[Int]]): Seq[Seq[Int]] = xss match {
    // last element given
    case heads +: Nil   => heads.map(x => Seq(x))
    // non-last element given
    case heads +: tails => map2(heads, transpose(tails)) { case (x, y) => x +: y }
    // empty dataset given
    case _              => Seq.empty
  }

  val real = transpose(data)
  println(real == expected)

}
