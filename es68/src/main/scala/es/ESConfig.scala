package es

import com.sksamuel.elastic4s.http.ElasticNodeEndpoint
import com.sksamuel.elastic4s.http.ElasticProperties

object ESConfig {

  case class ElasticNodeProperties(protocol: String, host: String, port: Int)

  case class AppProperties(es: ElasticNodeProperties)

  import pureconfig._
  import pureconfig.generic.auto._

  private lazy val config =
    ConfigSource
      .resources("application.conf")
      .load[AppProperties]
      .getOrElse(throw new IllegalArgumentException("config read error"))

  private lazy val es = config.es

  private lazy val options: Map[String, String] = Map.empty

  private lazy val nodes: Seq[ElasticNodeEndpoint] = Seq(ElasticNodeEndpoint(es.protocol, es.host, es.port, None))

//  val props: ElasticProperties     = ElasticProperties("http://localhost:9200")
  lazy val props: ElasticProperties = ElasticProperties(nodes, options)

}
