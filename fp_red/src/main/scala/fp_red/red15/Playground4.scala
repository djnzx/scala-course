package fp_red.red15

import scala.util.chaining.scalaUtilChainingOps

object Playground4 extends App {

  import SimpleStreamTransducers.Process
  import SimpleStreamTransducers.Process._

  val s = (1 to 10).to(Stream)

  /** pack numbers in list of N */
  def repackBy(n: Int): Process[Int, List[Int]] =
    loops(List.empty[Int]) { (x: Int, buf) =>
      if (buf.length == n) (Some(buf), List(x))
      else (None, buf :+ x)
    } { buf => Some(buf) }

  /** pack numbers in list of N */
  def repackByV2(n: Int): Process[Int, List[Int]] =
    loops2(List.empty[Int]) {
      case (buf, Some(x)) =>
        if (buf.length == n) (Some(buf), List(x))
        else (None, buf :+ x)
      case (buf, None) => (Some(buf), buf)
    }

  repackBy  (4)(s).toList.pipe(println)
  repackByV2(4)(s).toList.pipe(println)

}
