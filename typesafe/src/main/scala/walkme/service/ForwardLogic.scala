package walkme.service

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.{Http, HttpExt}
import akka.util.ByteString
import walkme.service.LoadBalancer._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

object ForwardLogic {
  sealed trait Command
  case class RequestAJoke(sender: ActorRef[String]) extends Command

  val BASE_PORT = 9550
  def target(n: Int) = (BASE_PORT + n) match {
    case port => s"http://localhost:$port/get-fortune"
  }

  def apply(implicit system: ActorSystem[_]): Behavior[Command] = {
    import system.executionContext
    val http: HttpExt = Http()
    def doGet(n: Int) = http
      .singleRequest(HttpRequest(uri = target(n)))
      .flatMap(_.entity.dataBytes.runFold(ByteString(""))(_ ++ _))
      .map(_.utf8String)

    val clients = Seq(1, 2, 3)
    val hc = new HttpClient[Any, String] {
      override def mkGet(rq: Any, id: Int)(implicit ec: ExecutionContext): Future[String] = doGet(id)
    }
    val b = Balancer.create(clients, hc)

    Behaviors.receiveMessage {
      case RequestAJoke(sender) =>
        // TODO insert logic here
        b.onRequest(_: Any, m => sender ! m)
          .onComplete(_ => Behaviors.same)
//        doGet(1)
//          .andThen {
//            case Success(m)  => sender ! m
//            case Failure(ex) => system.log.info(s"Remote request failed with $ex")
//          }
        Behaviors.same
    }
  }
}
