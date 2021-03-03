package fp_red.red15

object StateStreamTransducers {
  
  sealed trait SProcess[I, O, S] {
    
    /** interpreter */
    def apply(is: Stream[I]): Stream[O] = { 
      ???
//      this match {
//      case SProcess.Halt(s, f)         => Stream.empty
//      case SProcess.Emit(h, s, f, is)  => h #:: t(is)
//      case SProcess.Await(s, f)        => is match {
//        case i #:: is => f(Some(i), s)(is)
//        case _        => f(None,    s)(Stream.empty)  
//      }
    }
    
  }

  /** Stream Processing With Passing Intermediate steps between stages */
  object SProcess {
    
    type Transform[I, O, S] = (Option[I], Option[S]) => SProcess[I, O, S]

    /** state 1/3: we are done, can terminate stream */
    case class Halt[I, O, S](
      s: Option[S],            /** state                  */
      f: Transform[I, O, S]    /** transform function     */
    ) extends SProcess[I, O, S]

    /** state 2/3: we are ready to emit the value */
    case class Emit[I, O, S](
      head: O,                 /** element which is ready */
      s: Option[S],            /** state                  */
      f: Transform[I, O, S],   /** transform function     */
      tail: SProcess[I, O, S]  /** tail to be processed   */
    ) extends SProcess[I, O, S]

    /** state 3/3: we are awaiting for the next element to combine */
    case class Await[I, O, S](
      s: Option[S],            /** state                  */
      f: Transform[I, O, S],   /** transform function     */
    ) extends SProcess[I, O, S]

  }

  def main(args: Array[String]): Unit = {
    println(3)
  }

}
