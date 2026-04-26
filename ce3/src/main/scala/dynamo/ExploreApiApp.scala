package dynamo

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.syntax.all._
import java.net.URI
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import org.scanamo._
import org.scanamo.generic.auto._
import org.scanamo.syntax._
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model._

object ExploreApiApp extends IOApp.Simple {

  final case class Data(
    userId: String,
    email: String,
    age: Int
  )

  val tableName = "data"

  val datas: Table[Data] = Table[Data](tableName)

  def dynamoClient: Resource[IO, DynamoDbAsyncClient] =
    Resource.fromAutoCloseable {
      IO.blocking {
        DynamoDbAsyncClient
          .builder()
          .region(Region.US_EAST_1)
          // not required to connect, but required by SDK
          .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("dummy", "dummy")))
          // docker run -p 8000:8000 amazon/dynamodb-local
          .endpointOverride(URI.create("http://localhost:8000"))
          .build()
      }
    }

  def waitUntilTableExists(client: DynamoDbAsyncClient): IO[Unit] = {

    def check: IO[Boolean] =
      IO.fromCompletableFuture {
        IO.delay {
          client.describeTable(
            DescribeTableRequest
              .builder()
              .tableName(tableName)
              .build()
          )
        }
      }.map(_.table().tableStatus() == TableStatus.ACTIVE)

    check.flatMap {
      case true  => IO.unit
      case false => IO.sleep(500.milliseconds) *> waitUntilTableExists(client)
    }
  }

  def createTableIfNotExists(client: DynamoDbAsyncClient): IO[Unit] = {
    val request =
      CreateTableRequest
        .builder()
        .tableName(tableName)
        .attributeDefinitions(
          AttributeDefinition
            .builder()
            .attributeName("userId")
            .attributeType(ScalarAttributeType.S)
            .build()
        )
        .keySchema(
          KeySchemaElement
            .builder()
            .attributeName("userId")
            .keyType(KeyType.HASH)
            .build()
        )
        .billingMode(BillingMode.PAY_PER_REQUEST)
        .build()

    IO.fromCompletableFuture(IO.delay(client.createTable(request)))
      .attempt
      .flatMap {
        case Right(_)                        => IO.println(s"Table $tableName created") *> waitUntilTableExists(client)
        case Left(_: ResourceInUseException) => IO.println(s"Table $tableName already exists")
        case Left(error)                     => IO.raiseError(error)
      }
  }

  def insertUser(scanamo: ScanamoCats[IO], user: Data): IO[Unit] =
    scanamo
      .exec(datas.put(user))
      .void

  def fetchUser(scanamo: ScanamoCats[IO], userId: String): IO[Option[Either[DynamoReadError, Data]]] =
    scanamo.exec(datas.get("userId" === userId))

  def program(client: DynamoDbAsyncClient): IO[Unit] = {
    val scanamo = ScanamoCats[IO](client)

    val user =
      Data(
        userId = "u-001",
        email = "alex@example.com",
        age = 42
      )

    for {
      _      <- createTableIfNotExists(client)
      _      <- insertUser(scanamo, user)
      result <- fetchUser(scanamo, "u-001")
      _      <- IO.println(result)
    } yield ()
  }

  override def run: IO[Unit] =
    dynamoClient.use(program)

}
