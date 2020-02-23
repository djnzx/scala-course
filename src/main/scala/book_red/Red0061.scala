package book_red

import scala.util.Random

object Red0061 extends App {
  1 to 10 map { _ => new Random(42) } map { _.nextInt(100) } foreach println
}
