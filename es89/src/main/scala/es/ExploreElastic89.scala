package es

import cats._
import cats.implicits._
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties, RequestFailure, RequestSuccess, Response}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.sksamuel.elastic4s.requests.indexes.admin.DeleteIndexResponse
import com.sksamuel.elastic4s.requests.indexes.{CreateIndexRequest, CreateIndexResponse, DeleteIndexRequest, IndexRequest, IndexResponse}
import com.sksamuel.elastic4s.requests.mappings.MappingDefinition
import com.sksamuel.elastic4s.requests.mappings.dynamictemplate.DynamicMapping
import com.sksamuel.elastic4s.requests.searches.SearchRequest
import com.sksamuel.elastic4s.requests.searches.SearchResponse
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

import scala.concurrent.ExecutionContext.Implicits.global

class ExploreElastic89 extends AnyFunSuite with BeforeAndAfterAll {

  val props: ElasticProperties = ElasticProperties("http://localhost:9200")
  val client: ElasticClient = ElasticClient(JavaClient(props))

  override protected def afterAll(): Unit = client.close()

  test("delete index") {
    val rqDeleteIdx: DeleteIndexRequest = deleteIndex("artists")
    val x: Response[DeleteIndexResponse] = client.execute(rqDeleteIdx).await
    pprint.pprintln(x)
  }

  test("create index with mapping - strict") {
    val mapDef: MappingDefinition = properties(
      textField("name")
    ).dynamic(DynamicMapping.Strict)
    val rqCreateIdx: CreateIndexRequest = createIndex("artists").mapping(mapDef)
    val x: Response[CreateIndexResponse] = client.execute(rqCreateIdx).await
    pprint.pprintln(x)
  }

  test("insert into index - good") {
    val rqInsertIntoIndex: IndexRequest = indexInto("artists").fields("name" -> "Jim").refresh(RefreshPolicy.Immediate)
    val x: Response[IndexResponse] = client.execute(rqInsertIntoIndex).await
    pprint.pprintln(x)
  }

  test("insert into index - extra field") {
    val rqInsertIntoIndex: IndexRequest = indexInto("artists").fields("name" -> "Beam", "t" -> 1).refresh(RefreshPolicy.Immediate)
    val x: Response[IndexResponse] = client.execute(rqInsertIntoIndex).await
    pprint.pprintln(x)
  }

  test("query index") {
    val rqSearch: SearchRequest = search("artists").query("jim")
    val resp: Response[SearchResponse] = client.execute(rqSearch).await

    resp match {
      case failure: RequestFailure => pprint.pprintln("We failed " -> failure.error)
      case results: RequestSuccess[SearchResponse] => pprint.pprintln(results.result.hits.hits.toList)
      case results => pprint.pprintln(results.result)
    }

    resp.foreach((x: SearchResponse) => println("There were" -> x.totalHits))
  }

}
