package google_logging

import com.google.cloud.MonitoredResource
import com.google.cloud.logging.{LogEntry, Logging, LoggingOptions, Payload, Severity}
import com.google.cloud.logging.Payload.{JsonPayload, StringPayload}
import com.google.protobuf.Struct

import java.util.{Collections, UUID}
import scala.jdk.CollectionConverters.MapHasAsJava
import scala.language.implicitConversions

/**
  * Since we are in the Google Cloud,
  * probably it will be better to use Google Log Collector
  *
  * TODO: 1. ENABLE Google Cloud structured logs COLLECTOR (fluentd, ...)
  * TODO: 2. CREATE a few dummy services which will log all HTTP requests and forward them to each other to test that functionality
  * TODO: 3. CONNECT to Google Cloud log collector. Authentication.
  * TODO: 4. clarify DEPENDENCIES list. (alongside with "com.google.cloud" % "google-cloud-logging" % "3.0.1")
  * TODO: 5. QUERY LOGS collected
  * TODO: 6. DEFINE the GognitOpsLogEntry data structure. What data needs to be logged
  * TODO: 7. IMPLEMENT the mapper GognitOpsLogEntry = GoogleCloudLogEntry
  * TODO: 8. IMPLEMENT convenient Scala wrapper (facade) to avoid even one extra call
  * TODO: 9. IMPLEMENT convenient Scala "lifter" to keep using our logging in a structured way, but not detailed
  * TODO: 10. setup REPRESENTATION LAYER (Grafana, standard google cloud, etc)
  * TODO: 11. metrics
  * TODO: 12. alerts
  *
  * related docs:
  * https://cloud.google.com/logging/docs/structured-logging
  * https://cloud.google.com/logging/docs/reference/libraries#logging_write_log_entry-java
  * https://sematext.com/blog/best-log-management-tools/
  * https://medium.com/@rtwnk?p=3ef27e67e7ee
  */
object LoggingApplication extends App {

  val logging: Logging = LoggingOptions.getDefaultInstance.getService

  case class MyDetails(serviceName: String, dcId: String)

  // TODO: what that ???
  val logName = "my-log"

  val jsonPayload: Payload[Struct] = JsonPayload.of(Map(
    "service" -> "ingest",
    "dcId" -> UUID.randomUUID().toString
  ).asJava)

  // The data to write to the log
  val textPayload: Payload[String] = StringPayload.of(
    "Hello, world!"
  )

  // TODO: what that ???
  val resource = MonitoredResource
    .newBuilder("global")
    .build

  val entry: LogEntry = LogEntry
    .newBuilder(jsonPayload)
    .setSeverity(Severity.ERROR)
    .setLogName(logName)
    .setResource(resource)
    .build

  implicit def oneToMany(e: LogEntry) = Collections.singleton(e)

  logging.write(entry)
}
