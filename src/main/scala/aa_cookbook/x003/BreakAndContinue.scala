package aa_cookbook.x003

import util.control.Breaks._

object BreakAndContinue extends App {

  /**
    * break example
    */
  breakable {
    for (i <- 1 to 10) {
      print(s"$i ")
      if (i == 5) break
    }
  }

  /**
    * continue example
    */
  for (i <- 1 to 10) {
    breakable {
      for (j <- i to 10) {
        if (j > 7) break
      }
    }
  }

}
