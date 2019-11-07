package x00warmup

// https://en.wikipedia.org/wiki/Levenshtein_distance
object Levenshtein extends App {
  def distance(src:String, dst:String, debug: Boolean = false): Int = {
    def min3(i1: Int, i2: Int, i3: Int): Int = scala.math.min(i1, scala.math.min(i2, i3))
    // creating structure
    val cost = new Array[Array[Int]](src.length + 1) // rows (size of src + 1)
    for (row <- 0 to src.length) cost(row) = new Array[Int](dst.length + 1) // cols (size of dst)
    def print_matrix: Unit = {
      // header
      for {
        col <- 0 to dst.length // horizontal (dst)
      } {
        val cell = if (col == 0) "      " else s" ${dst(col-1)} "
        print(cell)
        if (col == dst.length) println()
      }
      // body
      for {
        row <- 0 to src.length // outer (lines)
        col <- 0 to dst.length // inner (columns in line)
      } {
        val col0cell = if (col > 0) "" else if (row == 0) "   " else s" ${src(row-1)} "
        print(col0cell)
        print(s" ${cost(row)(col)} ")
        if (col == dst.length) println
      }
    }
    def print_line: Unit = println("-".repeat((dst.length+2)*3))
    // initial data
    for (row <- 0 to src.length) cost(row)(0) = row // fill vertical line
    for (col <- 0 to dst.length) cost(0)(col) = col // fill horizontal line
    // algorithm
    for (
      row <- 1 to src.length;
      col <- 1 to dst.length
    ) cost(row)(col) = min3(
        cost(row-1)(col  ) + 1,
        cost(row  )(col-1) + 1,
        cost(row-1)(col-1) + (if (src(row-1) == dst(col-1)) 0 else 1),
      )
    if (debug) {
      print_line
      print_matrix
      print_line
      println(s"Levenshtein distance between '$src' and '$dst' is: ${cost(src.length)(dst.length)}")
    }
    cost(src.length)(dst.length)
  }
  distance("surgeon", "urgently", debug = true)
}
