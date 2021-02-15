package fp_red.red12

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
  val xs = (1 to 100000).map(x => if(x%2 == 0)-x else x).toList
//  val xs = List(0,1,2,3,-3,-4,-5,-6,0)

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
  val r1 = count10(initial)
  print("A:"); println(r1)

  /** fold data structure to the VALUE */
  val r2: Counter = xs.foldLeft(initial) { (acc, x) => count(x)(acc) }
  print("B1:"); println(r2)
  
  val r2a = (zero: Counter) => xs.foldLeft(zero) { (acc, x) => count(x)(acc)}
  val r2ar = r2a(initial)
  print("B2:"); println(r2ar)
  
  import fp_red.red06.State

  /** fold data structure to the FUNCTION via State monad, Stack safe */
  val f30: List[State[Counter, Unit]] = xs.map { State.modify[Counter] _ compose count }
  val f3: State[Counter, List[Unit]] = State.sequence(f30)
  /** run (fold) */
  val r3t = f3.run(initial)
  /** extract the result */
  val r3 = r3t._2
  print("C:"); println(r3)

  /** fold data structure to the FUNCTION via State monad, Stack safe */
  def f4(inputs: List[Int]): State[Counter, Counter] = {
    val ss: List[State[Counter, Unit]] = inputs.map { x => State.modify[Counter](count(x)) }
    val sq: State[Counter, List[Unit]] = State.sequence(ss) // it has stack safe implementations
    for {
      _ <- sq
      s <- State.get
    } yield s
  }
  val r4 = f4(xs).run(initial)._1
  print("D:"); println(r4)

  /** fold data structure to the FUNCTION, not stack safe */
  val f5: Counter => Counter = xs.foldLeft(identity[Counter] _) { (acc, x) => acc compose count(x) }
  /** actual calculation is HERE */
  val r5 = f5(initial)
  print("E:"); println(r5)
}
