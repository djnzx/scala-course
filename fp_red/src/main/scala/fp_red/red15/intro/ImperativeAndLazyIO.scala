package fp_red.red15.intro

import fp_red.red13.IO

object ImperativeAndLazyIO {

  /**
    * The problem: iside IO we can write whatever...
    */
  def linesGt40k(filename: String): IO[Boolean] = IO {
    val src = scala.io.Source.fromFile(filename)
    try {
      var count = 0
      val lines: Iterator[String] = src.getLines
      while (count <= 40000 && lines.hasNext) {
        lines.next // has side effect of advancing to next element
        count += 1
      }
      count > 40000
    } finally src.close
  }

  /**
    * ongoing ideas:
    */
  object Examples {
    val lines: Stream[String] = ???
    val ex1 = lines.zipWithIndex.exists(_._2 + 1 >= 40000)
    val ex2 = lines.filter(!_.trim.isEmpty).zipWithIndex.exists(_._2 + 1 >= 40000)
    val ex3 = lines.take(40000).map(_.head).indexOfSlice("abracadabra".toList)
  }

  /**
    * 1. Stream[String] inside the IO monad isn’t actually a pure value
    * 2. It will be close only after last line readed
    */
  def lines(filename: String): IO[Stream[String]] = IO {
    val src = scala.io.Source.fromFile(filename)
    src.getLines.to(Stream).lazyAppendedAll {
      src.close; Stream.empty
    }
  }
}
