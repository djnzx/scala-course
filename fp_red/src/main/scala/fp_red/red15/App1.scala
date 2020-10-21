package fp_red.red15

object App1 extends App {
  import SimpleStreamTransducers.Process
  import SimpleStreamTransducers.Process.convertFahrenheit
  
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
