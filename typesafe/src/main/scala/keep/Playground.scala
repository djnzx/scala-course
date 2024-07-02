package keep

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.RunnableGraph
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import scala.concurrent.Future

object Playground extends App {

  implicit val system: ActorSystem = ActorSystem("example")

  val source: Source[Int, NotUsed] = Source(1 to 10)
  val sink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  /** right, both - we have access to the result */
  val g_both: RunnableGraph[(NotUsed, Future[Int])] = source.toMat(sink)(Keep.both)
  val g_right: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)

  /** left, none - we don't have access to the result */
  val g_left: RunnableGraph[NotUsed] = source.toMat(sink)(Keep.left)
  val g_none: RunnableGraph[NotUsed] = source.toMat(sink)(Keep.none)

  val result1: (NotUsed, Future[Int]) = g_both.run()
  val result3: Future[Int] = g_right.run()

  val r_left: NotUsed = g_left.run()
  val r_none: NotUsed = g_none.run()

  import system.dispatcher
  result1._2.foreach(sum => println(s"Sum with Keep.both: $sum"))
  result3.foreach(sum => println(s"Sum with Keep.right: $sum"))

  system.terminate()

}
