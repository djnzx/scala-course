package walkme.service

import java.util.concurrent.atomic.AtomicReference

import walkme.thName

import scala.concurrent.impl.Promise
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object LoadBalancerCallBack {

  trait HttpClient[A, B] {
    def mkGet(rq: A, id: Int)(implicit ec: ExecutionContext): Future[B]
  }

  case class State[A, B] private(queue: List[(A, B => Any)], busy: Vector[Boolean]) {
    private def occupy(i: Int) = copy(busy = busy.updated(i, true))
    def release(i: Int) = copy(busy = busy.updated(i, false))
    private def nextAvailable = busy.indexWhere(_ == false) match {
      case x if x >= 0 => Some(x)
      case _ => None
    }
    def nonEmpty = queue.nonEmpty
    def enqueue(rq: A, cb: B => Any) = copy(queue = (rq, cb) :: queue)
    private def dequeue = queue match {
      case Nil => (this, None)
      case rs :+ r => (copy(queue = rs), Some(r))
    }
    private def dequeueOpt = dequeue match {
      case (_, None) => None
      case (s, Some(r)) => Some(s, r)
    }
    def dequeAndOccupyOpt =
      dequeueOpt // 1.1. check if we have something to process
        .flatMap { case (s, rh) => nextAvailable.map(i => (s, rh, i)) } // 1.2. get free workers if available
        .map { case (s, rh, i) => (s.occupy(i), rh, i) } // 1.3. occupy it
  }

  object State {
    def initial[A, B](n: Int) = new State(List.empty[(A, B => Any)], Vector.fill(n)(false))
  }

  class StateRef[A, B] private(n: Int) {
    private val ref = new AtomicReference(State.initial[A, B](n))
    def enqueue(rq: A, cb: B => Any): Unit = {
      ref.updateAndGet(_.enqueue(rq, cb))
      println(s"enqueued: $rq: ${ref.get().queue.map(_._1).mkString(" ")}")
    }
    def release(i: Int): Unit = ref.updateAndGet(_.release(i))
    def dequeue =
      Some(ref.get)
        .flatMap(_.dequeAndOccupyOpt)
        .map { case (s, rr, i) => ref.set(s); rr -> i }
    def nonEmpty = ref.get.nonEmpty
  }

  object StateRef {
    def create[A, B](n: Int) = new StateRef[A, B](n)
  }

  class Balancer[A, B] private(clients: Seq[Int], http: HttpClient[A, B]) {
    private val state = StateRef.create[A, B](clients.length)

    private def process(http: HttpClient[A, B])(implicit ec: ExecutionContext): Unit =
      state.dequeue.foreach { case ((rq: A, rh: (B => Any)), i) =>
        println(s"====> $rq dequeued to run on $i")
        println(s"doing HTTP Rq to ${clients(i)} at ${System.currentTimeMillis()}")
        http.mkGet(rq, clients(i))
          .map { rs => println(s"===> mkGet Response GOT: $rs"); rs }
          .map(rh(_))
          .andThen(_ => state.release(i))
          .onComplete(_ => process(http))
      }

    def handle(rq: A, cb: B => Any)(implicit ec: ExecutionContext): Future[Unit] =
      Future { println(s"==> onRequest $rq in $thName") }
        .andThen(_ => state.enqueue(rq, cb))
        .andThen(_ => process(http))

  }

  object Balancer {
    def create[A, B](clients: Seq[Int], http: HttpClient[A, B]) = new Balancer[A, B](clients, http)
  }

}
