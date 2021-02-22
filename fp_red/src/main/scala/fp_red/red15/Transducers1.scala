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
          case 2 => emit(buf, go1(List(i))) // emit List, and start from empty
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
          case _ => emit(buf)
        }
    }

    /** implementation based on the await helper function */
    def go2(buf: List[Int]): Process[Int, List[Int]] = await(
      /** function to do the job */
      { i: Int =>
        buf.length match {
          /** buffer is full, emit buffer, and start with given element */
          case 2 => emit(buf, go2(List(i)))
          /** buffer isn't full, keep collecting */
          case _ => go2(buf :+ i)
        }
      },
      /** no more elements, check the buffer size */
      buf.length match {
        /** buffer is empty - terminate */
        case 0 => Halt()
        /** buffer is not empty - emit last element */
        case _ => emit(buf)
      }
    )
      
    go2(Nil)
  }
   
  val packed: Stream[List[Int]] = packBy2(source)
  pprint.pprintln(packed.toVector)
  
  val flatten: Process[List[Int], Int] = await { xs: List[Int] =>
        
    def go(xs: List[Int]): Process[List[Int], Int] = xs match {
      case Nil => Halt()
      case h :: t => emit(h, go(t))
    }       

    go(xs)
  }
  
  val flattened: Stream[Int] = flatten(packed)
  pprint.pprintln(flattened.toVector)

}

