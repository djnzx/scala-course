package transpose

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import transpose.MatrixTranspose.show

/** functional matrix transpose */
object MatrixTranspose {

  def map2[A, B, C](as: Seq[A], bs: Seq[B])(f: (A, B) => C): Seq[C] = {
    // we assume both of them of the same size
    val zipped: Seq[(A, B)] = as zip bs
    val remapped: Seq[C] = zipped.map(f.tupled)
    remapped
  }

  def transpose(xss: Seq[Seq[Int]]): Seq[Seq[Int]] = xss match {
    // last element given
    case Seq(heads)     => heads.map(x => Seq(x))
    // non-last element given
    case heads +: tails =>
      map2(heads, transpose(tails))((h, ts) => h +: ts)
    // empty dataset given
    case _              => Seq.empty
  }

  def show(xss: Seq[Seq[Int]]) =
    xss
      .map(_.mkString(" "))
      .foreach(println)

}

class MatrixTranspose extends AnyFunSuite with Matchers {

  import MatrixTranspose.transpose

  test("1") {

    val matrix = List(
      List(1, 2, 3),
      List(4, 5, 6)
    )

    val expected = List(
      List(1, 4),
      List(2, 5),
      List(3, 6)
    )

    println("-- original matrix --")
    show(matrix)
    println("-- transposed matrix --")
    val transposed = transpose(matrix)
    show(transposed)
    println("-- transposed matrix (expected) --")
    show(expected)

    transposed shouldBe expected
  }

}
