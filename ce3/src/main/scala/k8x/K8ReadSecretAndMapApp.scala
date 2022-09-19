package k8x

import cats.implicits.toFunctorOps
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.util.Config
import scala.jdk.CollectionConverters.MapHasAsScala

object K8ReadSecretAndMapApp extends App {
  final case class Namespace(value: String) extends AnyVal

  implicit val namespaceProd = Namespace("prod")
//  implicit val namespaceDefault = Namespace("default")

  val kConfig = Config.defaultClient
  val kApi = new CoreV1Api(kConfig)
  def obtainSecret(secretName: String)(implicit namespace: Namespace) = kApi
    .readNamespacedSecret(secretName, namespace.value, null)
    .getData
    .asScala
    .toMap
    .fmap(new String(_)) // secrets are Array[Byte]
  def obtainConfigMap(secretName: String)(implicit namespace: Namespace) = kApi
    .readNamespacedConfigMap(secretName, namespace.value, null)
    .getData
    .asScala
    .toMap

  val configMapNames = List(
//    "achievement-saver-cm",
//    "yt-access-token-service-configs",
//    "yt-channel-data-api-cm"
  )
  val secretNames = List(
//    "dynamo-secret",
//    "asknicely-secret",
//    "iterable-secret",
//    "postgres-read-secret",
    "postgres-app-secret"
//    "s3-secret"
  )

  val secrets = secretNames.map(obtainSecret)
  val configMaps = configMapNames.map(obtainConfigMap)

  List(secrets, configMaps).flatten
    .flatMap(_.toList)
    .foreach { case (k, v) => println(s"$k -> $v") }
}
