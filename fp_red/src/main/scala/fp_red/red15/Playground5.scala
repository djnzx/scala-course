package fp_red.red15

object Playground5 extends App {

  import SimpleStreamTransducers.Process
  import SimpleStreamTransducers.Process._

  val p = emitSeq(List(1,2,3))
  val r = p(Stream.empty).toList
  pprint.pprintln(r)

  val src: Stream[Int] = (1 to 10).to(Stream)
  val p2: Process[Nothing, Int] = emitStream(src).filter(_ < 5)
  val r2 = p2(Stream.empty).toList
  pprint.pprintln(r2)

}