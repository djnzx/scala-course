package es68

import com.sksamuel.elastic4s.http.ElasticNodeEndpoint
import com.sksamuel.elastic4s.http.ElasticProperties

object ElasticConfig {

  case class ElasticNodeProperties(protocol: String, host: String, port: Int)
  case class AppProperties(es: ElasticNodeProperties)

  import pureconfig._
  import pureconfig.generic.auto._

//  val props: ElasticProperties     = ElasticProperties("http://localhost:9200")
  def props(fileName: String = "application.conf"): ElasticProperties = {
    val appProperties =
      ConfigSource
        .resources(fileName)
        .load[AppProperties]
        .getOrElse(throw new IllegalArgumentException("config read error"))
    val es = appProperties.es
    val options: Map[String, String] = Map.empty
    val nodes: Seq[ElasticNodeEndpoint] = Seq(ElasticNodeEndpoint(es.protocol, es.host, es.port, None))

    ElasticProperties(nodes, options)
  }

}
