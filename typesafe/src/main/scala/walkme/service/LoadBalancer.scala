package walkme.service

import java.util.concurrent.atomic.AtomicReference

import scala.concurrent.{ExecutionContext, Future}

object LoadBalancer {

  /** isolated state */
  case class State[A, B] private(queue: List[(A, B => Any)], busy: Vector[Boolean]) {
    private def occupy(i: Int) = copy(busy = busy.updated(i, true))
    def release(i: Int) = copy(busy = busy.updated(i, false))
    private def nextAvailable = busy.indexWhere(_ == false) match {
      case x if x >= 0 => Some(x)
      case _           => None
    }
    def nonEmpty = queue.nonEmpty
    def enqueue(rq: A, cb: B => Any) = copy(queue = (rq, cb) :: queue)
    private def dequeue = queue match {
      case Nil     => (this, None)
      case rs :+ r => (copy(queue = rs), Some(r))
    }
    private def dequeueOpt = dequeue match {
      case (_, None)    => None
      case (s, Some(r)) => Some(s, r)
    }
    def dequeAndOccupyOpt =
      dequeueOpt
        .flatMap { case (s, rh) => nextAvailable.map(i => (s, rh, i)) }
        .map { case (s, rh, i) => (s.occupy(i), rh, i) }
  }
  /** only one way to build initial state */
  object State {
    def initial[A, B](n: Int) = new State[A, B](List.empty, Vector.fill(n)(false))
  }
  /** thread safety proxy to state */
  class StateRef[A, B] private(n: Int) {
    private val ref = new AtomicReference(State.initial[A, B](n))
    def enqueue(rq: A, cb: B => Any): Unit = ref.updateAndGet(_.enqueue(rq, cb))
    def release(i: Int): Unit = ref.updateAndGet(_.release(i))
    def dequeueAndOccupy =
      Some(ref.get)
        .flatMap(_.dequeAndOccupyOpt)
        .map { case (s, rr, i) => ref.set(s); rr -> i }
    def nonEmpty = ref.get.nonEmpty
  }
  /** the only way to create it */
  object StateRef {
    def create[A, B](n: Int) = new StateRef[A, B](n)
  }
  /** interface to do the job */
  trait HttpClient[A, B] {
    def mkGet(rq: A, id: Int)(implicit ec: ExecutionContext): Future[B]
  }

  class Balancer[A, B] private(clients: Seq[Int], http: HttpClient[A, B]) {
    private val state = StateRef.create[A, B](clients.length)

    private def process(http: HttpClient[A, B])(implicit ec: ExecutionContext): Unit =
      state.dequeueAndOccupy.foreach { case ((rq: A, rh: (B => Any)), i) =>
        println(s"Sending request to the instance ${i+1}")
        http.mkGet(rq, clients(i))
          .map(rh(_))
          .andThen(_ => state.release(i))
          .andThen(_ => process(http))
      }

    def handle(rq: A, cb: B => Any)(implicit ec: ExecutionContext) =
      Future { state.enqueue(rq, cb) }
        .andThen(_ => process(http))

  }
  /** the only way to create a balancer */
  object Balancer {
    def create[A, B](clients: Seq[Int], http: HttpClient[A, B]) = new Balancer[A, B](clients, http)
  }

}
