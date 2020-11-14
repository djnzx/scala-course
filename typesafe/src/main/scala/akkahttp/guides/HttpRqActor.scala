package akkahttp.guides

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.{Http, HttpExt}
import akka.http.scaladsl.model._
import akka.util.ByteString

class HttpRqActor extends Actor with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  implicit val system = context.system
  val http: HttpExt = Http(system)

  override def preStart() =
    http
      .singleRequest(HttpRequest(uri = "http://localhost:9551/get-fortune"))
      .pipeTo(self)

  override def receive = {
        
    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        log.info("Got response, body: " + body.utf8String)
      }
      
    case resp @ HttpResponse(code, _, _, _) =>
      log.info("Request failed, response code: " + code)
      resp.discardEntityBytes()
      
  }
}
