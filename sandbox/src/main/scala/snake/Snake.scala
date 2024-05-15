package snake

import common.Base

object Snake {

  def sequential(w: Int, h: Int, x: Int, y: Int): Int =
    y * w + (x + 1)

  def zigzag(w: Int, h: Int, x: Int, y: Int): Int = {
    val x1 = x + 1
    val xInv = (w + 1) - x1
    val toRight = 1 - y % 2
    val toLeft = y      % 2

    y * w +
      x1 * toRight + // >>>
      xInv * toLeft  // <<<
  }

  def snake(w: Int, h: Int, x: Int, y: Int, next: Int = 1): Int =
    if (y == 0) next + x
    else snake(h - 1, w, y - 1, w - x - 1, next + w)

  def layers2(w: Int, h: Int, x: Int, y: Int) = {
    val layer = x min y min (w - (x + 1)) min (h - (y + 1))

    if      (layer == y)           2 * layer * (w + h - 2 * layer - 1) + (x - layer + 1) // top triangle with diagonals
    else if (layer == w - (x + 1)) 2 * layer * (w + h - 2 * layer - 1) + (w - 2 * layer - 1 + y - layer + 1) // right
    else if (layer == h - (y + 1)) 2 * layer * (w + h - 2 * layer - 1) + (w + h - 4 * layer - 3 + w - 1 - layer - x + 1) // bottom
    else if (layer ==  x)          2 * layer * (w + h - 2 * layer - 1) + (2 * (w + h - 4 * layer - 2) + h - 1 - layer - y + 1) // left
    else ???
  }

  def layers(w: Int, h: Int, x: Int, y: Int) = {
    val layer = x min y min (w - 1 - x) min (h - 1 - y)
    if (y == layer)
      2 * layer * (w + h - 2 * layer - 1) + (x - layer + 1)
    else if (x == w - 1 - layer)
      2 * layer * (w + h - 2 * layer - 1) + (w - 2 * layer - 1 + y - layer + 1)
    else if (y == h - 1 - layer)
      2 * layer * (w + h - 2 * layer - 1) + (w + h - 4 * layer - 3 + w - 1 - layer - x + 1)
    else
      2 * layer * (w + h - 2 * layer - 1) + (2 * (w + h - 4 * layer - 2) + h - 1 - layer - y + 1)
  }

}

class Snake extends Base {

  import Snake._

  test("row 1") {
    snake(10, 6, 0, 0) shouldBe 1
    snake(10, 6, 1, 0) shouldBe 2
    snake(10, 6, 2, 0) shouldBe 3
    snake(10, 6, 3, 0) shouldBe 4
    snake(10, 6, 4, 0) shouldBe 5
    snake(10, 6, 5, 0) shouldBe 6
    snake(10, 6, 6, 0) shouldBe 7
    snake(10, 6, 7, 0) shouldBe 8
    snake(10, 6, 8, 0) shouldBe 9
    snake(10, 6, 9, 0) shouldBe 10
  }

  test("last column") {
    snake(10, 6, 9, 1) shouldBe 11
    snake(10, 6, 9, 2) shouldBe 12
    snake(10, 6, 9, 3) shouldBe 13
    snake(10, 6, 9, 4) shouldBe 14
    snake(10, 6, 9, 5) shouldBe 15
  }

  val w = 5
  val h = 4

  def run(impl: (Int, Int, Int, Int) => Int): Unit =
    (0 until h).foreach { y =>
      (0 until w).foreach { x =>
        print(" %2d".formatted(impl(w, h, x, y)))
      }
      println
    }

  test("sequential") {
    run(sequential)
    println()
  }

  test("zig-zag") {
    run(zigzag)
    println()
  }

  test("layers") {
    run(layers)
    println()
  }

  test("snake") {
    val w = 5
    val h = 4

    (0 until h).foreach { y =>
      (0 until w).foreach { x =>
        print(" %2d".formatted(snake(w, h, x, y)))
      }
      println
    }
  }

}
