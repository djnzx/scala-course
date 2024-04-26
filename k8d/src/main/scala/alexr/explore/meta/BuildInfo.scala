package alexr.explore.meta

trait BuildInfo {
  val name: String
  val version: String
  val scalaVersion: String
  val sbtVersion: String
  val branch: scala.Option[String]
  val hash: scala.Option[String]
  val builtAtString: String
  val builtAtMillis: scala.Long
}
