package akkatyped.x01

import akka.actor.typed.scaladsl.{ActorContext, Behaviors, LoggerOps}
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

final case class Greet(whom: String, sender: ActorRef[Greeted])
final case class Greeted(whom: String, sender: ActorRef[Greet])

object MsgSender {
  def apply(): Behavior[Greet] = Behaviors.receive { (context: ActorContext[Greet], message: Greet) =>
    // print message
    context.log.info("Sender. Got {}!", message)
    // send new message to the sender
    val me: ActorRef[Greet] = context.self
    val g = Greeted(message.whom, me)
    context.log.info2("Sending {} to {}", g, message.sender)
    message.sender ! g

    Behaviors.same
  }
}

object MsgResponderBot {

  def apply(max: Int): Behavior[Greeted] = bot(0, max)

  private def bot(greetingCounter: Int, max: Int): Behavior[Greeted] =
    Behaviors.receive { (context, message: Greeted) =>
      context.log.info("Bot. Got {}", message.whom)

      val n = greetingCounter + 1
      context.log.info2("Bot. Responded {} for {}", n, message.whom)
      if (n == max) {
        context.log.info("Bot. Stopping")
        Behaviors.stopped
      } else {
        val g = Greet(message.whom, context.self)
        context.log.info2("Bot. Sending {} to {}", g, message.sender)
        message.sender ! g

        bot(n, max)
      }
    }
}


object AkkaTypedFirstApp extends App {

  final case class Message(body: String)

  val init: Behavior[Message] = Behaviors.setup { context: ActorContext[Message] =>
    context.log.info("Runner. Spawning Sender")
    val sender: ActorRef[Greet] = context.spawn(MsgSender(), "sender")
    context.log.info("Runner. Sender spawned")

    Behaviors.receive { (context, message: Message) =>
      context.log.info("Runner. Got: {}. Spawning Bot and feed it with message.body", message)
      val bot: ActorRef[Greeted] = context.spawn(MsgResponderBot(2), message.body)
      context.log.info("Runner. Bot spawned: {}", bot)
      val g = Greet(message.body, bot)
      context.log.info("Runner. Sending {} to Sender", g)
      sender ! g
      Behaviors.same
    }
  }
  // setup system
  val system: ActorSystem[Message] = ActorSystem(init, "root")
  system.log.info("-------")

  system ! Message("AAA")
  system ! Message("BBB")

  Thread.sleep(1000)
  system.terminate
}




