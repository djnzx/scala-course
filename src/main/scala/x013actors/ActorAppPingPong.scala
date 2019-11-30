package x013actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

case class PingMessage(n: Int)
case object PongMessage
case object StartMessage
case object StopMessage

class Ping(pong: ActorRef) extends Actor {
  var count = 0
  def incAndPrint: Unit = { count+=1; println(s"ping $count") }

  override def receive: Receive = {
    case StartMessage =>
      incAndPrint
      pong ! PingMessage(count)
    case PongMessage =>
      if (count > 10) {
        sender ! StopMessage
        println("Ping Stopped")
        context.stop(self)
      } else {
        incAndPrint
        sender ! PingMessage(count)
      }
    case x =>
      println(s"Ping: Got something unexpected: $x")
  }
}

class Pong extends Actor {
  println("PONG: constructor")
  override def preStart(): Unit = println("PONG:starting")

  override def postStop(): Unit = println("PONG:stopped")

  override def receive: Receive = {
    case PingMessage(n: Int) =>
      println(s"   pong: $n")
      sender ! PongMessage
    case StopMessage =>
      println("Ping Stopped")
      context.stop(self)
    case x =>
      println(s"Pong: Got something unexpected: $x")
  }
}

object ActorAppPingPong extends App {
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name = "pong")
  val ping = system.actorOf(Props(new Ping(pong)), name = "ping")

  ping ! StartMessage

  system.terminate()
}
