package aa_cookbook.x003

import scala.util.control.Breaks

object NestedBreaks extends App {
  val outer = new Breaks
  val inner = new Breaks

  outer.breakable {
    for (i <- 1 to 10) {
      inner.breakable {
        for (j <- i to 10) {
          if (i % 3 == 0) inner.break
          if (i % 5 == 0) outer.break
        }
      }
    }
  }

}
