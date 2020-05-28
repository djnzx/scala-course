package akkatyped.x00b

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

sealed trait Event

sealed trait EventRoot extends Event
case object Terminate extends EventRoot
case class Just(n: Int) extends EventRoot

sealed trait EventMediator
case object Finish extends EventMediator
case object Shutdown extends EventMediator
case class AdderRs(n: Int) extends EventMediator
case class Go(n: Int) extends EventMediator

sealed trait EventAdder extends Event
case class FinishMe(me: ActorRef[EventMediator]) extends EventAdder
case class AdderRq(n: Int, replyTo: ActorRef[EventMediator]) extends EventAdder

object AdderActor {
  def apply(): Behaviors.Receive[EventAdder] = Behaviors.receiveMessage {
    case AdderRq(n, replyTo) =>
      val n100 = n + 100
      println(s"Adder: $n => $n100")
      replyTo ! AdderRs(n100)
      Behaviors.same
    case FinishMe(me) =>
      me ! Shutdown
      Behaviors.stopped
  }
}

object MediatorActor {
  def apply(adder: Option[ActorRef[EventAdder]]): Behaviors.Receive[EventMediator] = Behaviors.receive { (ctx, msg) =>
    (adder, msg) match {
      case (_, Shutdown) =>
        println("Simple. Shutting down")
        Behaviors.stopped
      case (Some(a), Finish) =>
        println("Simple. Stopping")
        a ! FinishMe(ctx.self)
        Behaviors.same

      case (None, Go(n)) =>
        println(s"Simple: $n")
        val adder = ctx.spawn(AdderActor(), "adder")
        adder ! AdderRq(n, ctx.self)
        apply(Some(adder))
      case (Some(a), Go(n)) =>
        println(s"Simple: $n")
        a ! AdderRq(n, ctx.self)
        Behaviors.same

      case (_, AdderRs(n)) =>
        println(s"Simple:Response got: $n")
        Behaviors.same
    }
  }
}

object RootActor {
  def apply(mediator: Option[ActorRef[EventMediator]]): Behaviors.Receive[EventRoot] = Behaviors.receive { (ctx: ActorContext[EventRoot], msg: EventRoot) =>
    (mediator, msg) match {
      case (Some(h), Terminate) =>
        println("Root. Terminate")
        h ! Finish
        Behaviors.stopped

      case (None, Just(n)) =>
        println(s"Root. Just($n) got. Simple initialization and Send Go")
        val mediator = ctx.spawn(MediatorActor(None), "simple")
        mediator ! Go(n)
        apply(Some(mediator))

      case (Some(h), Just(n)) =>
        println(s"Root. Just($n) got. Send Go. ")
        h ! Go(n)
        apply(mediator)
    }
  }
}

object AkkaAdderApp extends App {
  val root = ActorSystem(RootActor(None), "root")

  (1 to 3).map(Just).foreach(root ! _)

  root ! Terminate
}

