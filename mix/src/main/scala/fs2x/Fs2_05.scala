package fs2x

import fs2._

object Fs2_05 extends App {
  /**
    * .scan
    * .scanChunks
    * .scanChunksOpt
    *
    * pipe implementation
    * (custom take)
    */
  def tk[F[_],O](n: Long): Pipe[F,O,O] =
                                           // S => Option[Chunk[O2] => (S, Chunk[O3])]
    in => in.scanChunksOpt(n) { n: Long => // intentional shadowing
      if (n <= 0) None                     // None - stop iteration
      else Some((c: Chunk[O]) => c.size match {
        case m if m < n => (n - m, c              ) // (new state N, chunk)
        case _ =>          (0,     c.take(n.toInt)) //
      })
    }

  def tk2[F[_],O](n: Long): Pipe[F,O,O] = {
    def go(s: Stream[F,O], n: Long): Pull[F,O,Unit] = {
      s.pull.uncons.flatMap {
        case Some((hd,tl)) =>
          hd.size match {
            case m if m <= n => Pull.output(hd) >> go(tl, n - m)
            case _ => Pull.output(hd.take(n.toInt)) >> Pull.done
          }
        case None => Pull.done
      }
    }
    in => go(in,n).stream
  }
  val r1: List[Int] = Stream(1,2,3,4,5,6,7).through(tk(4)).toList
  println(r1)
  val r2: List[Int] = Stream(1,2,3,4,5,6,7).through(tk2(4)).toList
  println(r2)

  Stream.range(0,100).takeWhile(_ < 7).toList
  // List[Int] = List(0, 1, 2, 3, 4, 5, 6)
  Stream("Alice","Bob","Carol").intersperse("|").toList
  // List[String] = List("Alice", "|", "Bob", "|", "Carol")
  Stream.range(1,10).scan(0)(_ + _).toList
  // List[Int] = List(0, 1, 3, 6, 10, 15, 21, 28, 36, 45)
}
