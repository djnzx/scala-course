package keep

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

class SourcesPlayground extends AnyFunSuite with BeforeAndAfterAll {

  implicit val system: ActorSystem = ActorSystem("example")
  import system.dispatcher

  implicit class AwaitOsp[A](fa: Future[A]) {
    def await: A = Await.result(fa, 10.seconds)
  }

  override protected def afterAll(): Unit = system.terminate()

  test("1") {
    val x = Source
      .future(Seq(1, 2, 3).pure[Future])
      .mapConcat(identity)
      .toMat(Sink.headOption)(Keep.right)
      .run
      .await

    pprint.log(x)
  }

}
