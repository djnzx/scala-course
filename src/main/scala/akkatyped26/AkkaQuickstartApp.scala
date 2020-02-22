package akkatyped26

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}

final case class FirstMessage(text: String)
final case class Message(text: String, replyTo: ActorRef[Response])
final case class Response(text: String, from: ActorRef[Message])

object ActorResponder {
  def apply(max: Int): Behavior[Response] = bot(0, max)

  private def bot(greetingCounter: Int, max: Int): Behavior[Response] = Behaviors.receive { (context, message) =>
    val n = greetingCounter + 1
    context.log.info("Responder got message {} and forwarding it {}", n, message.text)
    if (n == max) {
      Behaviors.stopped
    } else {
      message.from ! Message(message.text, context.self)
      bot(n, max)
    }
  }
}

object ActorSender {
  def apply(): Behavior[Message] = Behaviors.receive { (context, message: Message) =>
    context.log.info("Sender got message and forwarding: {}", message.text)
    message.replyTo ! Response(message.text, context.self)
    Behaviors.same
  }
}

object ActorRoot {
  def apply(): Behavior[FirstMessage] = Behaviors.setup { context: ActorContext[FirstMessage] =>
    val sender: ActorRef[Message] = context.spawn(ActorSender(), "sender")
    val b: Behaviors.Receive[FirstMessage] = Behaviors.receiveMessage { message: FirstMessage =>
      context.log.info("Root actor got message: {}", message.text)
      val replyTo: ActorRef[Response] = context.spawn(ActorResponder(max = 3), "responder")
      sender ! Message(message.text, replyTo)
      Behaviors.same
    }
    b
  }
}

/**
  * runner part.
  * 1. creating actor system
  * 2. sending first message
  */
object AkkaQuickstartApp extends App {
  val greeterMain: ActorSystem[FirstMessage] = ActorSystem(ActorRoot(), "root")
  greeterMain ! FirstMessage("Rendezvous")
  //  Thread.sleep(1000)
  //  greeterMain terminate
}
