package fp_red.red15

import fp_red.red15.Playground0.convertFahrenheit

object Playground1 extends App {
  import SimpleStreamTransducers.Process
  
  val conv: Process[String, String] = convertFahrenheit
  
  val s =
    """
      |#hello
      |10
      |20
      |30
      |#world
      |""".stripMargin 
  val ss: Stream[String] = s.split("\n").toStream
  
  val r: Stream[String] = conv(ss)
  val r2 = r.toList
  pprint.pprintln(r2)
}
