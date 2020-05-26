package fp_red.red01_05

import scala.util.Random

object Red06 extends App {
  1 to 10 map { _ => new Random(42) } map { _.nextInt(100) } foreach println
}
