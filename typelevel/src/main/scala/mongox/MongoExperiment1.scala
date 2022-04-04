package mongox

import cats.effect._
import cats.implicits._
import org.mongodb.scala.bson.BsonObjectId

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.util.chaining._

object MongoExperiment1 extends IOApp {

  val blocker = Executors
    .newCachedThreadPool()
    .pipe(ExecutionContext.fromExecutor)
    .pipe(Blocker.liftExecutionContext)

  val resource: Resource[IO, MongoConnector[IO]] =
    MongoConnector.resource[IO](blocker, MongoConfig("root:mn123456@127.0.0.1", 27017, "admin"))

  override def run(args: List[String]): IO[ExitCode] =
    resource
      .use(experiment)
      .as(ExitCode.Success)

  val id: BsonObjectId = BsonObjectId("624b095c37895b3be3a370b7")

  def experiment(mc: MongoConnector[IO]): IO[Any] =
    mc.queryMany("1")(
      _.getCollection("phone")
        .find()
//        .find(Filters.equal("_id", id))
        .map(d => d("name").asString().getValue),
    ).flatMap(_.traverse_(x => IO(println(x))))

}
