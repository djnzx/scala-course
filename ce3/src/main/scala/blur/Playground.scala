package blur

import scala.reflect.ClassTag

object ComonadImageBlurExample extends App {

  trait Comonad[F[_]] {
    def extract[A](fa: F[A]): A

    def coflatMap[A, B: ClassTag](fa: F[A])(f: F[A] => B): F[B]

    def map[A, B: ClassTag](fa: F[A])(f: A => B): F[B] =
      coflatMap(fa)(wa => f(extract(wa)))
  }

  final case class Grid[A](
    data: Array[Array[A]],
    row: Int,
    col: Int
  ) {
    def height: Int = data.length
    def width: Int = if (data.isEmpty) 0 else data(0).length
    def at(r: Int, c: Int): A = data(r)(c)
    def extract: A = data(row)(col)
    def moveTo(r: Int, c: Int): Grid[A] = copy(row = r, col = c)
  }

  // --------------------------------------------
  // 3. Comonad instance for Grid
  //    coflatMap builds a new grid by focusing
  //    each cell and applying f there
  // --------------------------------------------
  implicit val gridComonad: Comonad[Grid] = new Comonad[Grid] {

    def extract[A](fa: Grid[A]): A = fa.extract

    def coflatMap[A, B: ClassTag](fa: Grid[A])(f: Grid[A] => B): Grid[B] = {
      val out =
        Array.tabulate(fa.height, fa.width) { (r, c) =>
          f(fa.moveTo(r, c))
        }

      Grid(out, fa.row, fa.col)
    }
  }

  // --------------------------------------------
  // 4. Blur utilities
  // --------------------------------------------

  // Collect 3x3 neighborhood around current focus.
  // Border handling: clamp coordinates to image bounds.
  def neighborhood3x3(grid: Grid[Int]): Vector[Int] = {
    val rs = Vector(grid.row - 1, grid.row, grid.row + 1)
    val cs = Vector(grid.col - 1, grid.col, grid.col + 1)

    for {
      r <- rs
      c <- cs
    } yield {
      val rr = math.max(0, math.min(r, grid.height - 1))
      val cc = math.max(0, math.min(c, grid.width - 1))
      grid.at(rr, cc)
    }
  }

  // Simple blur: arithmetic mean of 3x3 neighborhood
  def blurPixel(grid: Grid[Int]): Int = {
    val ns = neighborhood3x3(grid)
    ns.sum / ns.size
  }

  def blurImage(image: Array[Array[Int]]): Array[Array[Int]] = {
    val focused = Grid(image, 0, 0)
    gridComonad.coflatMap(focused)(blurPixel).data
  }

  // --------------------------------------------
  // 5. Pretty printing
  // --------------------------------------------
  def printImage(title: String, image: Array[Array[Int]]): Unit = {
    println(title)
    image.foreach { row =>
      println(row.map(v => f"$v%3d").mkString(" "))
    }
    println()
  }

  // --------------------------------------------
  // 6. Concrete dataset
  //    Bright center pixel on dark background
  // --------------------------------------------
  val input: Array[Array[Int]] = Array(
    Array(10, 10, 10, 10, 10),
    Array(10, 50, 50, 50, 10),
    Array(10, 50, 255, 50, 10),
    Array(10, 50, 50, 50, 10),
    Array(10, 10, 10, 10, 10)
  )
//  val input: Array[Array[Int]] = Array(
//    Array(0, 0, 0, 0, 0),
//    Array(0, 0, 0, 0, 0),
//    Array(0, 0, 255, 0, 0),
//    Array(0, 0, 0, 0, 0),
//    Array(0, 0, 0, 0, 0)
//  )

  val blurred = blurImage(input)

  printImage("Input image:", input)
  printImage("Blurred image:", blurred)


}
