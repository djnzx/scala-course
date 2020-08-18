package http

import java.net.URLEncoder

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object HttpApp extends App {
  implicit val system: ActorSystem = ActorSystem()
  import system.dispatcher

  val codeToHightlight =
    """
      |object SimpleApp {
      |  val aField = 2
      |
      |  def aMethod(x: Int) = x + 1
      |
      |  def main(args: Array[String]) = {
      |    println(aMethod(aField))
      |  }
      |}
""".stripMargin

  def toEntity(content: String) = HttpEntity(
    ContentTypes.`application/x-www-form-urlencoded`,
    s"source=${URLEncoder.encode(content.trim, "UTF-8")}&language=Scala&theme=Sunburst"
  )
  
  def highlight(code: String): Future[String] =
    Http()
      .singleRequest(
        HttpRequest(HttpMethods.POST, uri = "http://markup.su/api/highlighter", entity = toEntity(code))
      )
      .flatMap(_.entity.toStrict(2 seconds))
      .map(_.data.utf8String)
  
  val highlighted: String = Await.result(highlight(codeToHightlight), 10 seconds)
  println(highlighted)
  Await.result(system.terminate(), 10 seconds);
}
