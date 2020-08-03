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
