package cookbook.x011

object C11_06 extends App {
  val stream = 1 #:: 2 #:: 3 #:: Stream.empty // scala.collection.immutable.Stream[Int]
  val stream2 = (1 to 1000000000).toStream
  val stream3 = (1 to 1000000000).to(LazyList)
  stream.head
  stream.tail
  stream.take(3)
  val s2 = stream2.filter(_ < 100)
  stream2.map(x => x + 2)

  val ab = Array(1,5,6,72,11)

}
