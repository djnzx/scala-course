package fp_red.red15

object StateStreamTransducers {
  
  sealed trait SProcess[I, O, S] {
    
    /** interpreter */
    def apply(is: Stream[I]): Stream[O] = this match {
      /** normal termination without state */
      case SProcess.Halt(None,    _)       => Stream.empty
      /** normal termination WITH state */
      /** WHAT's TODO WITH STATE */
      case SProcess.Halt(Some(s), f)       => ??? 
      /** ready to emit */
      /** WHAT's TODO WITH STATE */
      case SProcess.Emit(head, s, f, tail) => head #:: tail(is)
      /** await for the next element */
      case SProcess.Await(s, f)            => is match {
        case i #:: is         => f(Some(i), s)(is)
        case e @ Stream.Empty => f(None,    s)(e) 
      }
    }
    
  }

  /**
    * Stream Processing With Passing Intermediate steps between stages
    * on each stage we need to pass intermediate state, and reducer function
    */
  object SProcess {
    
    /** our core function which can deal with any case */
    type Transform[I, O, S] = (Option[I], Option[S]) => SProcess[I, O, S]
    
    /**
      * state 1/3: we are done, can terminate stream
      * 
      * we can have "non empty" state, and we need to handle it 
      */
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
