package chi

import java.util.UUID

import scala.collection.mutable

/**
  * Disclaimer.
  * this is the Proof of Concept
  * 
  * - Async anc Concurrent stuff haven't been taken into account.
  * - we assume that spawning an instance and terminating them have zero overhead.
  * Actually, we need to use more sophisticated algorithm
  * and bring to the table something like moving average to start our instances in advance
  * by analyzing all active sessions and probably spawn new instance in advance  
  * 
  * Proper concurrent data types should be used.
  */
object ScareProxy extends App {
  /** type aliases */
  type Time = Long
  type SessionID = UUID
  
  /** current time in seconds, because containers being billed by seconds */
  def now: Time = System.currentTimeMillis() / 1000 // seconds
  val WORK_TIME = 30 * 60 // seconds
  val WAIT_TIME = 15 * 60 // seconds, we don't need it, just to note
  val N_WORKERS = 48      //          we don't need it, just to note
  val THROUGHPUT = 8

  /** session state */
  trait State
  case object Working extends State
  case object Waiting extends State
  
  /** Scare State */
  case class SState(state: State, at: Time)
  
  /** incoming Request to process (all GET and POST) */
  abstract class Request(s: SessionID, val t: Time = now)
  case class RqStartResume(s: SessionID) extends Request(s)
  case class RqSubmitOrGet(s: SessionID) extends Request(s)
  case class RqEnd        (s: SessionID) extends Request(s)
  
  /** Instance API provided by Instance implementation */
  trait Instance {
    def doTheRealJob(rq: Request): Unit
  }
  
  /** Cloud API provided by vendor */
  trait Cloud {
    def spawn: Instance
    def terminate(i: Instance): Unit
  }
  
  /** our routing implementation */
  class Proxy(c: Cloud) {
    // state of our sessions
    val sessions = mutable.Map.empty[SessionID, SState]
    // load of our instances
    val pool = mutable.Map.empty[Instance, Int]

    def sessionsActive = sessions.count { case (_, ss) => ss.state == Working }
    def instancesActive = pool.size
    def instancesNeed = math.ceil(sessionsActive.toDouble / THROUGHPUT).toInt
    /** we can terminate any instance which is out of the work */
    def pickToTerminate = pool.find { case (_, 0) => true }.map(_._1)
    /** logic for picking instance for forwarding */
    def pickToForward = pool
      .foldLeft((-1, Option.empty[Instance])) {           // -1, because we need to handle start case 
        case (r      , (_, THROUGHPUT))  => r             // we don't need fully loaded instances  
        case ((mx, _), (i, c)) if c > mx => (mx, Some(i)) // logic 
        case (r, _)                      => r             // if it less loaded than already found
      }._2
    def spawnIfNeed() = while (instancesNeed > instancesActive) pool.addOne(c.spawn -> 0)
    def shrinkIfCan() = while (instancesNeed < instancesActive) pickToTerminate.foreach(pool.remove)
    /** actually, it should be implemented as a chain of future calls */
    def forward(r: Request) = 
      pickToForward.foreach { instance =>                         // pick instance to forward to
        pool.updateWith(instance) { case Some(c) => Some(c + 1) } // increment load
        instance.doTheRealJob(r)                                  // send the request
        pool.updateWith(instance) { case Some(c) => Some(c - 1) } // decrement load
      }
    def handleRequest(r: Request) = r match {
      case RqStartResume(s) => markWorking(s, r.t); spawnIfNeed(); forward(r)
      case RqSubmitOrGet(s) => forward(r); markWaitingIfNeed(s, r.t)
      case RqEnd        (s) => forward(r); terminateSession(s); shrinkIfCan()
    }
    def terminateSession(s: SessionID) = sessions.remove(s)
    def markWorking(s: SessionID, t: Time) = sessions.update(s, SState(Working, t))
    def markWaiting(s: SessionID, t: Time) = sessions.update(s, SState(Waiting, t))
    /**
      * actually it should be done in a different way
      * for example during processing each request
      * and checking current time and first activity of each active session
      */
    def markWaitingIfNeed(s: SessionID, t: Time) = if (sessions(s).at == WORK_TIME) markWaiting(s, t)
  }
  
  
}
