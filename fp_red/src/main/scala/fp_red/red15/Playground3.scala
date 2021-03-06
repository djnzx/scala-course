package fp_red.red15

object Playground3 extends App {
  import SimpleStreamTransducers._
  import SimpleStreamTransducers.Process._

  val src = Stream(1,2,3,4,5).map(_.toDouble)
  val p: Process[Double, Double] = sum
  val dst = p(src)
  pprint.pprintln(dst.toList)
}
