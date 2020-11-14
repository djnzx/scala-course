package walkme

import java.util.concurrent.Executors

import walkme.LoadBalancer._

import scala.concurrent.{ExecutionContext, Future}

object LoadBalanceApp extends App {

  case class Rq(num: Int) extends Request
  def handler(rq: Request)(rs: Response) = println(s"===> handling response: $rq => $rs")
  /** usage */
  def app = {
    val clients = Seq(9551, 9552, 9553)
    val es = Executors.newFixedThreadPool(clients.length)
    implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(es)
    
    val httpClient: HttpClient = new TestHttpClient 
    val b: Balancer = Balancer.create(clients, httpClient)
    (0 to 9)
      .map(Rq)
      .foreach { rq =>
        Thread.sleep(10)
        println(s"=> generating $rq in thread $thName")
        b.onRequest(rq, handler(rq))
      }
    Thread.sleep(13000)
    es.shutdown()
  }
  
  app
}
