package fp_red.red15

import fp_red.red15.SimpleStreamTransducers.Process
import fp_red.red15.SimpleStreamTransducers.Process.{emitOne, lift, liftOne, filter, sum}

object TransducersExperiments extends App{
  /** whatever given, produce int val `1` */
  val emit1: Process[Any, Int] = emitOne(1)

  /** we can lift plain function A=>B to Process[A, B] */
  val orig_fn   : Int => String        = (i: Int) => i.toString
  val lift1_fn  : Process[Int, String] = liftOne { orig_fn }
  val liftAll_fn: Process[Int, String] = lift { orig_fn }
  /** stream transformed by process */
  val stream2: Stream[String] = lift1_fn(Stream(1,2,3))
  val stream3: Stream[String] = liftAll_fn(Stream(1,2,3))

  pprint.log(stream2.toList)
  pprint.log(stream3.toList)

  val evensf = filter((x: Int) => x % 2 == 0)
  val evens: List[Int] = evensf(Stream(1,2,3,4)).toList
  pprint.log(evens)
  val s = sum(Stream(1.0, 2.0, 3.0, 4.0)).toList
  pprint.log(s)
  
  
  val f: Process[String, Int] = lift { s: String => s.toInt}
  val g: Process[Int, Double] = lift { i: Int => i.toDouble }
  val h: Process[String, Double] = f |> g 
}
