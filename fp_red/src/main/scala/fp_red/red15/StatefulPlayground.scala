package fp_red.red15

object StatefulPlayground extends App {
  
  import StatefulStreamTransducers._
  import StatefulStreamTransducers.Process._
  
  val sa = Stream.from(5).take(3)
  
//  val p1: Process[Int, String, Nothing] = emitOne("J")
//  val r1 = p1(sa)
//  
//  val p2: Process[Int, String, Nothing] = emitSeq(Seq("A", "B", "C"))
//  val r2 = p2(sa)
//
//  val p3: Process[Int, Int, Nothing] = emitStream(Stream.from(11).take(3))
//  val r3 = p3(sa)
//  
//  val p4: Process[Int, Int, Nothing] = liftOne((_: Int) + 10)
//  val r4 = p4(sa)
//  
//  val p5: Process[Int, Int, Nothing] = lift((_: Int) + 10)
//  val r5 = p5(sa)
//  r5.foreach(println)
  
}
