package d09_concurrent

import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._

import java.util.concurrent.atomic.AtomicReference
import scala.annotation.tailrec

object A3_RefUpdateImpure extends IOApp {

  trait ModifyImplementation[A, B] {

    val ar: AtomicReference[A]

    def modify(f: A => (A, B)): IO[B] = {

      @tailrec
      def spin: B = {
        val current = ar.get
        val (updated, b) = f(current)

        /** check whether current wasn't modified.
          * that's why function can be called MORE than once !!!
          * so, it needs to be PURE and FAST !!!
          */
        if (!ar.compareAndSet(current, updated)) spin
        else b
      }

      IO(spin)
    }
  }

  def task(taskId: Int, ref: Ref[IO, Int]): IO[Unit] =
    ref
      /** has optimistic strategy */
      .modify(prev => taskId -> println(s"t:$taskId -> v:$prev")) // <2>
      .replicateA(3) // <3>
      .void

  def run(args: List[String]): IO[ExitCode] =
    for {
      ref <- Ref[IO].of(0)
      _ <- List(1, 2, 3).parTraverse(taskId => task(taskId, ref)) // <1>
    } yield ExitCode.Success

}
