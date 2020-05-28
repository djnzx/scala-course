package akka_x

import akka.actor.{Actor, ActorSelection, ActorSystem, PoisonPill, Props}

case class Name(name: String)
case class CreateChild(name: String)

class Child extends Actor {
  var name = "Not given"

  override def postStop(): Unit = println(s"They killed me($name): ${self.path}")

  override def receive: Receive = {
    case Name(name_) => this.name = name_
    case _ => println(s"Child got message")
  }
}

class Parent extends Actor {
  override def postStop(): Unit = println(s"Somebody killed me: ${self.path}")

  override def receive: Receive = {
    case CreateChild(name) =>
      println(s"Parent about to create child: $name ...")
      val child = context.actorOf(Props[Child], name)
      child ! Name(name)
    case _ => println(s"Parent got other message")
  }
}

object ActorParentChild extends App {
  val system = ActorSystem("demo")
  val parent = system.actorOf(Props[Parent], "Parent")

  parent ! CreateChild("Jim")
  parent ! CreateChild("John")
  Thread.sleep(1000)

  println("looking for Jim...")
  val jim: ActorSelection = system.actorSelection("/user/Parent/Jim")
  jim ! PoisonPill
  println("Jim killed")
  Thread.sleep(4000)


  // all actors are going to be deleted right now
  system.terminate
}
