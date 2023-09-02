package slickvt

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.{Keep, Sink, Source}
import org.scalatest.concurrent.ScalaFutures

trait StreamToValueSyntax { _: ScalaFutures =>

  implicit class StreamTestOps[A](as: Source[A, NotUsed]) {

    def value()(implicit mat: Materializer): Seq[A] =
      as.toMat(Sink.seq)(Keep.right)
        .run()
        .futureValue

  }

}
