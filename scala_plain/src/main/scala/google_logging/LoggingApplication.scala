package google_logging

import com.google.cloud.MonitoredResource
import com.google.cloud.logging.{LogEntry, Logging, LoggingOptions, Severity}
import com.google.cloud.logging.Payload.StringPayload

import java.util.Collections

/**
  * TODO: 1. how to ENABLE fluentd COLLECTOR in Google Cloud
  * TODO: 2. how / where to SPECIFY connection details from my app to the Google Cloud Collector Engine
  * TODO: 3. what else dependencies I need to have in classpath?
  *  alongside with "com.google.cloud" % "google-cloud-logging" % "3.0.1"
  * TODO: 4. how to setup REPRESENTATION LAYER (Grafana, standard google cloud, etc)
  * TODO: 5. how to QUERY LOGS collected
  * TODO: 6. IMPLEMENT Scala wrapper
  * TODO: 7. IMPLEMENT Scala "lifter" from standard strings being written to the Console to structured LogEntries
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

  // The name of the log to write to
  val logName = "my-log"

  // The data to write to the log
  val text = "Hello, world!"

  val entry = LogEntry
    .newBuilder(StringPayload.of(text))
    .setSeverity(Severity.ERROR)
    .setLogName(logName)
    .setResource(
      MonitoredResource
        .newBuilder("global")
        .build
    )
    .build

  logging.write(Collections.singleton(entry))

  System.out.printf("Logged: %s%n", text)
}
