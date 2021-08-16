package google_logging

import com.google.cloud.MonitoredResource
import com.google.cloud.logging.{LogEntry, Logging, LoggingOptions, Payload, Severity}
import com.google.cloud.logging.Payload.{JsonPayload, StringPayload}
import com.google.protobuf.Struct

import java.util.{Collections, UUID}
import scala.jdk.CollectionConverters.MapHasAsJava
import scala.language.implicitConversions

/**
  * TODO: 1. how to ENABLE fluentd COLLECTOR in Google Cloud
  * TODO: 2. create innocent service which will log all http requests to test that functionality
  * TODO: 3. how / where to SPECIFY connection details from my app to the Google Cloud Collector Engine
  * TODO: 4. what else dependencies I need to have in classpath? alongside with "com.google.cloud" % "google-cloud-logging" % "3.0.1"
  * TODO: 5. how to setup REPRESENTATION LAYER (Grafana, standard google cloud, etc)
  * TODO: 6. how to QUERY LOGS collected
  * TODO: 7. IMPLEMENT convenient Scala wrapper (facade)
  * TODO: 8. IMPLEMENT Scala "lifter" from standard strings being written to the Console to structured LogEntries
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
