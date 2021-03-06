package fp_red.red15

object Playground2 extends App {
  import SimpleStreamTransducers.Process

  //                                initial state
  val source: Stream[Int] = Stream.unfold(0) {
    case n if n <= 5 =>
      print(".")
      //  element, next state
      Some((n, n + 1))
    case _ => None
  }
  println("source constructed (lazily)") // not completely lazy. we created the first element

  val process: Process[Int, String] = Process.lift((x: Int) => s"<<${x * 2}>>")
  println("transformer constructed (lazily)")

  val processed: Stream[String] = process.apply(source)
  println("transformer applied (lazily)")  // not completely lazy. we touched the first element

  println("running .toList on the result")
  pprint.pprintln(processed.toList) // one less because 1st already evaluated

  print("zipping:")
  val s2 = source zip Stream.from(100)
  println("zipped (lazily)")

  pprint.pprintln(s2.toList)
}
