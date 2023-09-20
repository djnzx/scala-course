package es68

import com.sksamuel.elastic4s.IndexAndType
import com.sksamuel.elastic4s.RefreshPolicy
import com.sksamuel.elastic4s.http.{ElasticClient, HttpResponse, RequestFailure, RequestSuccess, Response}
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.index.CreateIndexResponse
import com.sksamuel.elastic4s.http.index.IndexResponse
import com.sksamuel.elastic4s.http.index.admin.DeleteIndexResponse
import com.sksamuel.elastic4s.http.search.SearchResponse
import com.sksamuel.elastic4s.indexes.CreateIndexRequest
import com.sksamuel.elastic4s.indexes.DeleteIndexRequest
import com.sksamuel.elastic4s.indexes.IndexRequest
import com.sksamuel.elastic4s.mappings.MappingDefinition
import com.sksamuel.elastic4s.mappings.dynamictemplate.DynamicMapping
import com.sksamuel.elastic4s.searches.SearchRequest
import com.sksamuel.elastic4s.searches.queries.BoolQuery
import com.sksamuel.elastic4s.searches.queries.term.TermQuery
import es68.campaign.model.TargetType.{Instant, Registration, TargetList}
import io.circe.generic.AutoDerivation
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.slf4j.{Logger, LoggerFactory}

class ExploreElastic68Tests extends AnyFunSuite with BeforeAndAfterAll {

  val nativeProps = ElasticConfig.props("campaign.conf")
  val nativeClient: ElasticClient = ElasticClient(nativeProps)

  type EsIndex = String
  type EsType  = String
  val myIndex: EsIndex             = "artists"
  val myType: EsType               = "myType"
  val myIndexAndType: IndexAndType = myIndex / myType

  override protected def afterAll(): Unit = nativeClient.close()

  test("delete index") {
    val rqDeleteIdx: DeleteIndexRequest  = deleteIndex(myIndex)
    val x: Response[DeleteIndexResponse] = nativeClient.execute(rqDeleteIdx).await
    pprint.pprintln(x)
  }

  test("create index with mapping - strict") {

    val mapDef: MappingDefinition = mapping(myType)
      .fields(
        textField("name")
      )
      .dynamic(DynamicMapping.Strict)

    val rqCreateIdx: CreateIndexRequest  = createIndex(myIndex).mappings(mapDef)
    val x: Response[CreateIndexResponse] = nativeClient.execute(rqCreateIdx).await
    pprint.pprintln(x)
  }

  test("insert into index - by fields - good") {
    val rqInsertIntoIndex: IndexRequest = indexInto(myIndexAndType)
      .fields("name" -> "Ben")
      .refresh(RefreshPolicy.Immediate)

    val x: Response[IndexResponse] = nativeClient.execute(rqInsertIntoIndex).await
    pprint.pprintln(x)
  }

  test("insert into index - by JSON - good") {

    case class MyData(name: String)
    object MyData extends AutoDerivation

    val data = MyData("Doe33")

    import es68.IndexableDerivation.indexableWithCirce

    val rqInsertIntoIndex: IndexRequest = indexInto(myIndexAndType)
      .doc(data)

    val x: Response[IndexResponse] = nativeClient.execute(rqInsertIntoIndex).await
    pprint.pprintln(x)
  }

  test("insert into index - by fields - extra field - FAILED due to Strictness") {
    val rqInsertIntoIndex: IndexRequest =
      indexInto(myIndexAndType)
        .fields("name" -> "Beam", "t" -> 1)
        .refresh(RefreshPolicy.Immediate)

    val x: Response[IndexResponse] = nativeClient.execute(rqInsertIntoIndex).await
    pprint.pprintln(x)
  }

  test("query index") {
    val rqSearch: SearchRequest        = searchWithType(myIndexAndType).query("Doe33")
    val resp: Response[SearchResponse] = nativeClient.execute(rqSearch).await

    resp match {
      case failure: RequestFailure                 => pprint.pprintln("We failed " -> failure.error)
      case results: RequestSuccess[SearchResponse] => pprint.pprintln(results.result.hits.hits.toList)
      case results                                 => pprint.pprintln(results.result)
    }

    resp.foreach((x: SearchResponse) => println("There were" -> x.totalHits))
  }

  test("client123") {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.Future
    import es68.IndexableDerivation.hitReaderWithCirce
    import es68.campaign.model.CampaignEntity
    import ESClient._
    import es68.campaign.model.ChronoUnitInstances._

    val client = ESClient.apply[Future]("campaign.conf")
    implicit val logger: Logger = LoggerFactory.getLogger(this.getClass)

    val tQuery: TermQuery = termQuery("targetType", Instant.entryName)
    val bQuery: BoolQuery = must(tQuery)

    val indexAndType = "paridirect_campaign/campaign"
    val q: SearchRequest = search(indexAndType)//.query(bQuery)
    val xs: TotalResult[CampaignEntity] = client.execute0[SearchRequest, SearchResponse](q)
      .map(_.toTotalResult[CampaignEntity]).await

    pprint.pprintln(xs.total)
    pprint.pprintln(xs.data.size)
//    xs.foreach(x => pprint.pprintln(x))
//    client.close()

  }

}
