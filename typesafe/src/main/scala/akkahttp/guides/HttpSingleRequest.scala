package akkahttp.guides

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.{Http, HttpExt}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.util.ByteString

import scala.util.{Failure, Success}

object HttpSingleRequest extends App {

  implicit val akka = ActorSystem(Behaviors.empty, "SingleRequest")
  implicit val ec = akka.executionContext
  val http: HttpExt = Http() 
  
  http
    .singleRequest(HttpRequest(uri = "http://localhost:9551/get-fortune"))
    .onComplete {
      case Success(res) => 
        res.entity.dataBytes.runFold(ByteString(""))(_ ++ _)
          .map(_.utf8String)
          .foreach(println)
      case Failure(_) => sys.error("something wrong")
    }

}
