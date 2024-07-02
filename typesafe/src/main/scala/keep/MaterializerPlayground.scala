package keep

import akka.Done
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import cats.implicits.catsStdInstancesForFuture
import cats.implicits.catsSyntaxApplicativeId
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class MaterializerPlayground extends AnyFunSuite with BeforeAndAfterAll {

  implicit val system: ActorSystem = ActorSystem("example")

  import system.dispatcher

  override protected def afterAll(): Unit =
    system.terminate()

  val xs = List(1, 2, 3)

  val s = Source(xs)
    .mapAsync(1) { x => pprint.log(x); (x + 1).pure[Future] }

  /** Sink.seq - collect all to Seq[A] */

  /** run effects + return Future[A] */
  test("Sink.seq / Keep.both") {
    val x: (NotUsed, Future[Seq[Int]]) =
      s.toMat(Sink.seq)(Keep.both)
        .run()

    val r = Await.result(x._2, 10.seconds)
    pprint.log(r)
  }

  /** run effects + return Future[A] */
  test("Sink.seq / Keep.right") {
    val x: Future[Seq[Int]] =
      s.toMat(Sink.seq)(Keep.right)
        .run()

    val r: Seq[Int] = Await.result(x, 10.seconds)
    pprint.log(r)
  }

  /** run effects + return NotUsed */
  test("Sink.seq / Keep.left") {
    val x: NotUsed =
      s.toMat(Sink.seq)(Keep.left)
        .run()
  }

  /** run effects + return NotUsed */
  test("Sink.seq / Keep.none") {
    val x: NotUsed =
      s.toMat(Sink.seq)(Keep.none)
        .run()
  }

  /** Sink.ignore - no way to access results */

  /** run effects + return Future[Done] */
  test("Sink.ignore / Keep.both") {
    val x: (NotUsed, Future[Done]) =
      s.toMat(Sink.ignore)(Keep.both)
        .run()
  }

  /** run effects + return Future[Done] */
  test("Sink.ignore / Keep.right") {
    val x: Future[Done] =
      s.toMat(Sink.ignore)(Keep.right)
        .run()
  }

  /** run effects + return NotUsed */
  test("Sink.ignore / Keep.left") {
    val x: NotUsed =
      s.toMat(Sink.ignore)(Keep.left)
        .run()
  }

  /** run effects + return NotUsed */
  test("Sink.ignore / Keep.none") {
    val x: NotUsed =
      s.toMat(Sink.ignore)(Keep.none)
        .run()
  }

}
