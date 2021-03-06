package fp_red.red15

import fp_red.red15.SimpleStreamTransducers.Process
import fp_red.red15.SimpleStreamTransducers.Process._

/**
  * this use-case covers grouping
  * Stream[A] => Stream[B], B <: Seq[A]
  */
object Transducers1 extends App {
  val source: Stream[Int] = (1 to 15).to(Stream)
  pprint.pprintln(source.toVector)
  
  val packBy2: Process[Int, List[Int]] = {
    
    /** implementation based on the Await case class */
    def go1(buf: List[Int]): Process[Int, List[Int]] = Await {
      /** function to do the job */
      case Some(i) =>
        buf.length match {
          /** buffer is full, emit buffer, and start with given element */
          case 2 => emitOne(buf, go1(List(i))) // emit List, and start from empty
          /** buffer isn't full, keep collecting */
          case _ => go1(buf :+ i)
        }
      /** no more elements */
      case None =>
        /** check the buffer size */
        buf.length match {
          /** buffer is empty - terminate */
          case 0 => Halt()
          /** buffer is not empty - emit last element */
          case _ => emitOne(buf)
        }
    }

    /** implementation based on the await helper function */
    def go2(buf: List[Int]): Process[Int, List[Int]] = await(
      /** function to do the job, element will be given by the interpreter */
      { i: Int =>
        buf.length match {
          /** buffer is full, emit buffer, and start with given element */
          case 2 => emitOne(buf, go2(List(i)))
          /** buffer isn't full, keep collecting */
          case _ => go2(buf :+ i)
        }
      },
      /** no more elements, check the buffer size */
      buf.length match {
        /** buffer is empty - terminate */
        case 0 => Halt()
        /** buffer is not empty - emit last element */
        case _ => emitOne(buf)
      }
    )
      
    go2(Nil)
  }
   
  val packed: Stream[List[Int]] = packBy2(source)
  pprint.pprintln(packed.toVector)

  /** make one nested list flatten
    * it's recursive in terms on nested structure
    */
  def go(xs: List[Int]): Process[List[Int], Int] = xs match {
    case Nil => Halt()
    case h :: t => emitOne(h, go(t))
  }

  /** xs will be fed by `driver`, each item: List(1, 2), List(3, 4), ...  */
  val flatten1: Process[List[Int], Int] = await[List[Int], Int]             { xs => go(xs) }.repeat 
  val flatten2: Process[List[Int], Int] = lift(identity[List[Int]]).flatMap { xs => go(xs) }

  val flattened1: Stream[Int] = flatten1.apply(packed)
  val flattened2: Stream[Int] = flatten2.apply(packed)
  pprint.pprintln(flattened1.toVector)
  pprint.pprintln(flattened2.toVector)
  
  // TODO: implement random string packing by number of lines
  // TODO: implement random string packing by resulting char number
}

