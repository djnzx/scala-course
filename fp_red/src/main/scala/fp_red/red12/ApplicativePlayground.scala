package fp_red.red12

import fp_red.red06.State

import java.util.Date
import fp_red.red09.p1impl.ReferenceTypes.Parser
import fp_red.red09.p1impl.Reference._

/**
  * Applicative - just a sequence
  * Monad - dynamic, based on the prev. result
  */
object ApplicativePlayground {

  case class Row(date: Date, temperature: Double)

  val FA: Applicative[Parser] = ???
  val date: Parser[Date] = ???
  val temp: Parser[Double] = ???

  /**
    * 1/1/2010, 25
    * 2/1/2010, 28
    * 3/1/2010, 42
    * 4/1/2010, 53
    * ...
    *
    * to parse that - Applicative is enough
    * map2 - is a just predefined order
    */
  val row: Parser[Row] = FA.map2(date, temp)((d, t) => Row(d, t))
  val rows: Parser[List[Row]] = row.sep("\n")

  /**
    * if we don't know the order ->
    * first, we read the header, analyze it, and after that
    * produce the right parser.
    * We use information from header to build the resulting parser
    * rows2 can be built dynamically based on the header
    */
  val FM: Monad[Parser] = ???
  val header: Parser[Parser[Row]] = ???
  val rows2: Parser[List[Row]] = FM.flatMap (header) { row => row.sep("\n") }
}

object ApplicativePlaygroundApp0 extends App {
  val xs = List(1, 2, 3, -3, -4, -5, -6)
  /** count positive and count negative */
  val r = xs.foldLeft((0, 0)) { case ((n, p), x) => if (x > 0) (n, p + 1) else (n + 1, p) }
  println(r)
}

object ApplicativePlaygroundApp extends App {
  val xs = List(0,1,2,3,-3,-4,-5,-6,0)
  
  type Counter = (Int, Int, Int) // N, Z, P
  
  /** our state manipulation */
  def count(x: Int) = (c: Counter) => c match { case (n, z, p) => x match {
    case x if x > 0 => (n, z, p+1)
    case x if x < 0 => (n+1, z, p)
    case _          => (n, z+1, p)
  }}
  val initial = (0,0,0)
  
  /** function which will modify state with COUNTED 10 */
  val count10: Counter => Counter = count(10)
  val c10 = count10(initial)
  println(c10)
  
  /** fold data structure to the VALUE */
  val foldEagerly: Counter = xs.foldLeft(initial) { case (acc, x) => count(x)(acc) }
  println(foldEagerly)

  /** fold data structure to the FUNCTION */
  val foldLazy: Counter => Counter = xs.foldLeft(identity[Counter] _) { (acc, x) => acc compose count(x) }
  /** actual calculation is HERE */
  val r3 = foldLazy(initial)
  println(r3)

  import fp_red.red06.State

  /** fold data structure to the FUNCTION via State monad */
  val f3 = State.sequence(xs.map { State.modify[Counter] _ compose count } ) 
  // run and extract the result
  println(f3.run(initial)._2)

  def foldV4(inputs: List[Int]): State[Counter, Counter] = for {
    _ <- State.sequence(inputs map (State.modify[Counter] _ compose count))
    s <- State.get
  } yield s

  println(foldV4(xs).run(initial)._1)
}