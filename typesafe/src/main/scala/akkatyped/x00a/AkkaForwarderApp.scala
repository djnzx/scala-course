package akkatyped.x00a

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

sealed trait Message
case object Finish extends Message
case class Just(n: Int) extends Message

object SimpleActor {
  def apply(): Behaviors.Receive[Message] = Behaviors.receiveMessage {
    case Finish =>
      println("Simple.Stopping")
      Behaviors.stopped
    case Just(n) =>
      println(s"Simple: $n")
      Behaviors.same
  }
}

object RootBehavior {
  def apply(handler: Option[ActorRef[Message]]): Behaviors.Receive[Message] = Behaviors.receive { (ctx: ActorContext[Message], msg: Message) =>
    (handler, msg) match {
      case (None, Just(n)) =>
        println(s"Root. Just($n) Simple initialization")
        val simple = ctx.spawn(SimpleActor(), "simple")
        simple ! msg
        apply(Some(simple))
      case (Some(h), Just(n)) =>
        println(s"Root. Just($n)")
        h ! msg
        apply(handler)
      case (Some(h), Finish) =>
        println("Root. Shutdown")
        h ! Finish
        Behaviors.stopped
    }
  }
}

object AkkaForwarderApp extends App {
  val root = ActorSystem(RootBehavior(None), "root")

  (1 to 10).map(Just).foreach(m => root ! m)

  root ! Finish
}

