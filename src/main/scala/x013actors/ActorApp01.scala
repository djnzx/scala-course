package x013actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class HelloActor extends Actor {
  override def receive: Receive = {
    case "hello"   => println("I've got it")
    case "forward" => println(s"got 'forward' from $sender")
    case _         => println("?")
  }
}

class NamedActor(name: String, hell: ActorRef) extends Actor {
  override def receive: Receive = {
    case "hello" => println(s"$name: 'Hello' got it")
    case "FWD"   =>
      println(s"$name: got 'FWD' from $sender")
      hell ! "forward"
    case _       => println(s"$name: ?")
  }
}

object ActorApp extends App {
  // parent system creating
  val system: ActorSystem = ActorSystem("hello_system") // akka://hello_system/deadLetters

  // actor creating
  val hello_actor: ActorRef = system.actorOf(Props[HelloActor], name = "hello_actor")
  val fred_actor: ActorRef = system.actorOf(Props(new NamedActor("FRED", hello_actor)), name = "fred_actor")

  println(system)      // akka://hello_system
  println(hello_actor) // Actor[akka://hello_system/user/hello_actor#-978228995]
  println(fred_actor)  // Actor[akka://hello_system/user/fred_actor#1781372986]

  // these operations are async!
  hello_actor ! "forward" // got 'forward' from Actor[akka://hello_system/deadLetters]
  hello_actor ! "hello"
  hello_actor ! "Hi, there!"
  fred_actor ! "HI"
  fred_actor ! "FWD"

  system.terminate()
}
