package x013actors

import akka.actor.{Actor, ActorSystem, Props}

case object ForceRestart

class Kenny extends Actor {
  println("Kenny: constructor")

  override def preStart(): Unit = println("Kenny: preStart")

  override def postStop(): Unit = println("Kenny: postStop")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("Kenny: preRestart")
    println(s" MESSAGE: ${message.getOrElse("")}")
    println(s" REASON: ${reason.getMessage}")
    println("Kenny: preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("Kenny: postRestart")
    println(s" REASON: ${reason.getMessage}")
    println("Kenny: preRestart")
    super.postRestart(reason)
  }

  override def receive: Receive = {
    case ForceRestart => throw new Exception("got ForceRestart msg")
    case s: String => println(s"Kenny: got message(string) $s")
    case _ => println("Kenny: got message")
  }
}

object ScalaL03LifeCycle extends App {
  val system = ActorSystem("lifecycle")
  val kenny = system.actorOf(Props[Kenny], "kenny")
  println("MAIN: sending a simple message")
  kenny ! "hello"
  Thread.sleep(1000)

  println("MAIN: sending a second simple message")
  kenny ! "hello"
  Thread.sleep(1000)

  println("MAIN: sending ForceRestart")
  kenny ! ForceRestart
  Thread.sleep(1000)

  println("MAIN: stopping kenny")
  system.stop(kenny)
  Thread.sleep(1000)

  println("MAIN: system shutdown")
  system.terminate
}
