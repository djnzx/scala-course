package akkatyped.x00b

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

sealed trait Message

//sealed trait MediatorMessage
case object Finish extends Message
case object Shutdown extends Message
case class Just(n: Int) extends Message
case class AdderRs(n: Int) extends Message

//sealed trait AdderMessage extends Message
case class AdderRq(n: Int, replyTo: ActorRef[Message]) extends Message
case class FinishMe(me: ActorRef[Message]) extends Message


object AdderActor {
  def apply(): Behaviors.Receive[Message] = Behaviors.receiveMessage {
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
  def apply(adder: Option[ActorRef[Message]]): Behaviors.Receive[Message] = Behaviors.receive { (ctx, msg) =>
    (adder, msg) match {
      case (_, Shutdown) =>
        println("Simple. Shutting down")
        Behaviors.stopped
      case (Some(a), Finish) =>
        println("Simple. Stopping")
        a ! FinishMe(ctx.self)
        Behaviors.same
      case (None, Just(n)) =>
        println(s"Simple: $n")
        val adder = ctx.spawn(AdderActor(), "adder")
        adder ! AdderRq(n, ctx.self)
        apply(Some(adder))
      case (Some(a), Just(n)) =>
        println(s"Simple: $n")
        a ! AdderRq(n, ctx.self)
        Behaviors.same
      case (_, AdderRs(n)) =>
        println(s"Simple:Response got: $n")
        Behaviors.same
    }
  }
}

object RootBehavior {
  def apply(handler: Option[ActorRef[Message]]): Behaviors.Receive[Message] = Behaviors.receive { (ctx: ActorContext[Message], msg: Message) =>
    (handler, msg) match {
      case (Some(h), Finish) =>
        println("Root. Shutdown")
        h ! Finish
        Behaviors.stopped

      case (None, Just(n)) =>
        println(s"Root. Just($n) Simple initialization")
        val simple = ctx.spawn(MediatorActor(None), "simple")
        simple ! msg
        apply(Some(simple))

      case (Some(h), Just(n)) =>
        println(s"Root. Just($n)")
        h ! msg
        apply(handler)
    }
  }
}

object AkkaAdderApp extends App {
  val root = ActorSystem(RootBehavior(None), "root")

  (1 to 3).map(Just).foreach(root ! _)

  root ! Finish
}

