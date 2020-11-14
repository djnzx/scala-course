package walkme

import java.util.concurrent.atomic.AtomicReference

import scala.concurrent.{ExecutionContext, Future}

object LoadBalancer {
  trait Request {
    val num: Int
  }
  trait Response {
    val message: String
  }
  case class TestResponse(message: String) extends Response
  type ResponseHandler = Response => Any
  trait HttpClient {
    def mkGet(rq: Request, id: Int)(implicit ec: ExecutionContext): Future[Response]
  }
  class TestHttpClient extends HttpClient {
    override def mkGet(rq: Request, id: Int)(implicit ec: ExecutionContext) = Future {
      println(s"====> GET doing rq #${rq.num} to instance #$id in $thName")
      Thread.sleep(1000)
      println(s"====> GETisDONE rq #${rq.num} to instance #$id in $thName")
      TestResponse(s"RS: ${rq.num}")
    }
  }
  case class State private(queue: List[(Request, ResponseHandler)], busy: Vector[Boolean]) {
    private def occupy(i: Int) = {
      println(s"=====> $i is occupied")
      copy(busy = busy.updated(i, true))
    }
    def release(i: Int) = {
      println(s"=====< $i is released")
      copy(busy = busy.updated(i, false))
    }
    private def nextAvailable = busy.indexWhere(_ == false) match {
      case x if x >= 0 => Some(x)
      case _           => None
    }
    def nonEmpty = queue.nonEmpty
    def enqueue(rq: Request, cb: ResponseHandler) = {
      val s2 = copy(queue = (rq, cb) :: queue)
      println(s"===> $rq enqueued : "+ s2.queue.map(_._1).mkString(" "))
      s2
    }
    private def dequeue = queue match {
      case Nil     => (this, None)
      case rs :+ r => (copy(queue = rs), Some(r))
    }
    private def dequeueOpt = dequeue match {
      case (_, None)    => None
      case (s, Some(r)) => Some(s, r)
    }
    def dequeAndOccupyOpt =
      dequeueOpt                                                        // 1.1. check if we have something to process
        .flatMap { case (s, rh) => nextAvailable.map(i => (s, rh, i)) } // 1.2. get free workers if available
        .map { case (s, rh, i) => (s.occupy(i), rh, i) }                // 1.3. occupy it
  }
  object State {
    def initial(n: Int) = new State(Nil, Vector.fill(n)(false))
  }
  class StateRef private(n: Int) {
    private val ref = new AtomicReference(State.initial(n))
    def enqueue(rq: Request, cb: ResponseHandler): Unit = ref.updateAndGet(_.enqueue(rq, cb))
    def release(i: Int): Unit = ref.updateAndGet(_.release(i))
    
    def dequeue = 
      Some(ref.get)
      .flatMap(_.dequeAndOccupyOpt)
      .map { case (s, rr, i) => ref.set(s); rr -> i }

    def deqFn(s: State) = Some(s)
      .flatMap(_.dequeAndOccupyOpt)

    def dequeue2 = {
      ref.updateAndGet(s => deqFn(s) match {
        case Some((s2, _, _)) => s2 
        case None             => s
      })
    }

    def nonEmpty = ref.get.nonEmpty
  }
  object StateRef {
    def create(n: Int) = new StateRef(n)
  }
  class Balancer private(clients: Seq[Int], http: HttpClient) {
    private val state = StateRef.create(clients.length)

    private def process(http: HttpClient)(implicit ec: ExecutionContext): Unit = 
      state.dequeue.foreach { case ((rq, rh), i) =>
        println(s"====> $rq dequeued to run on $i")
        http.mkGet(rq, i)
          .map { rs => println(s"===> mkGet Response GOT: $rs") ;rs }
          .map(rh(_))
          .andThen(_ => state.release(i))
          .onComplete(_ => process(http))
      }

    def onRequest(rq: Request, cb: ResponseHandler)(implicit ec: ExecutionContext): Unit =
      Future {
        println(s"==> onRequest $rq in $thName")
        state.enqueue(rq, cb)
      }
        .andThen(_ => process(http))
      
  }
  object Balancer {
    def create(clients: Seq[Int], http: HttpClient) = new Balancer(clients, http)
  }

}
