package k8x

import io.circe.Encoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredEncoder
import io.circe.generic.semiauto.deriveEncoder
import io.circe.syntax.EncoderOps
import io.circe.yaml.syntax.AsYaml

object CirceYamlGenApp extends App {

  sealed trait KEnvEntryConfig
  object KEnvEntryConfig {
    private implicit val c = Configuration.default.copy(transformConstructorNames = _.deCapitalize)
    implicit val encoder: Encoder[KEnvEntryConfig] = deriveConfiguredEncoder[KEnvEntryConfig].mapJson(_.deepDropNullValues)
  }
  final case class SecretRef(name: String, extra: Option[String] = None) extends KEnvEntryConfig
  final case class ConfigMapRef(name: String) extends KEnvEntryConfig

  val base = "024948034611.dkr.ecr.us-east-1.amazonaws.com"
  val container = "achievements-milestone-backfill"
  val fullContainerName = base + "/" + container

  case class Job(
      name: String,
      containerName: String,
      envFrom: List[KEnvEntryConfig]
  )
  object Job {
    implicit val encoder: Encoder[Job] = deriveEncoder
  }

  val backfill = Job(
    "job-achievements-milestone-backfill",
    fullContainerName,
    List(
      SecretRef("qwe"),
      ConfigMapRef("asd")
    )
  )

  val s = backfill.asJson.asYaml.spaces2
  println(s)
}
