package mongox

import cats.effect._
import cats.implicits._
import org.mongodb.scala.bson.BsonObjectId
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Projections

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object MongoExperiment1 extends IOApp {

  def acquire: ExecutorService = Executors.newCachedThreadPool()

  def release(es: ExecutorService): Unit = es.shutdown()

  def mongo(b: Blocker) =
    //    MongoConnector.resource[IO](blocker, MongoConfig("root:mn123456@127.0.0.1", 27017, "admin"))
    MongoConnector.resource[IO](b, MongoConfig("127.0.0.1", 27017, "vidio-production"))

  def resource = Resource
    .make(IO(acquire))(es => IO(release(es)))
    .map(ExecutionContext.fromExecutor)
    .map(Blocker.liftExecutionContext)
    .flatMap(mongo)

  override def run(args: List[String]): IO[ExitCode] =
    resource
      .use(experiment)
      .as(ExitCode.Success)

  val id: BsonObjectId = BsonObjectId("624b095c37895b3be3a370b7")

  def experiment(mc: MongoConnector[IO]): IO[Any] =
    mc.queryMany(
      _.getCollection("users")
        .find(Filters.equal("admin", true))
//        .find(Filters.equal("dynamo_id", "Ai35s-2P2bvVpPgVX0fynw")),
//        .find(Filters.equal("_id", id))
//        .map(d => d("name").asString().getValue),
        .limit(5)
        .projection(Projections.include("first_name", "last_name"))
        .map { d =>
          (d("first_name").asString().getValue, d("last_name").asString().getValue)
        },
    ).flatMap(_.traverse_ { x =>
      IO(println(x))
    })

}
