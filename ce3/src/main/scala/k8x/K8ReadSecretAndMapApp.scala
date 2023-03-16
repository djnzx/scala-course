package k8x

import cats.implicits.toFunctorOps
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.models._
import io.kubernetes.client.util.Config

import scala.jdk.CollectionConverters.MapHasAsScala

object K8ReadSecretAndMapApp extends App {
  final case class Namespace(value: String) extends AnyVal

//  implicit val namespaceProd = Namespace("prod")
  implicit val namespaceDefault = Namespace("default")

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
    "fast-car"
  )
  val secretNames = List(
    "mysql"
  )

  val secrets = secretNames.map(obtainSecret)
  val configMaps = configMapNames.map(obtainConfigMap)

  List(secrets, configMaps).flatten
    .flatMap(_.toList)
    .foreach { case (k, v) => pprint.pprintln(s"$k -> $v") }

  new V1Pod
}
