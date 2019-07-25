package x013actors

import akka.actor.{Actor, ActorSystem, Props}

class HelloActor extends Actor {
  override def receive: Receive = {
    case "hello" => println("I've got it")
    case _       => println("?")
  }
}

object ActorApp extends App {
  // parent system
  val system = ActorSystem("hello_system")

  // our actor
  val hello_actor = system.actorOf(Props[HelloActor], name = "hello_actor")

  hello_actor ! "hello"
  hello_actor ! "Hi, there!"

  system.terminate()
}
