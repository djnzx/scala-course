package org.alexr.lx

import com.google.cloud.MonitoredResource
import com.google.cloud.logging._

import java.util.Collections
import java.util.{Set => JSet}
import scala.language.implicitConversions

/** https://cloud.google.com/logging/docs/quickstart-sdk
  * https://cloud.google.com/logging/docs/samples/logging-write-log-entry#logging_write_log_entry-java
  *
  * gcloud config set project beta-243321
  * gcloud logging write alexr-test-log "A simple entry."
  * gcloud logging write --payload-type=json alexr-test-log '{ "message": "My second entry", "weather": "partly cloudy"}'
  * gcloud logging read "resource.type=global"
  *
  * programmatic logging relies on env variable: GOOGLE_APPLICATION_CREDENTIALS
  */
object Main {

  def printEnv = {
    val key = System.getenv("GOOGLE_APPLICATION_CREDENTIALS")
    println(key)
  }

  def main(args: Array[String]): Unit = {
    printEnv

    implicit def oneToMany(x: LogEntry): JSet[LogEntry] = Collections.singleton(x)

    val logging: Logging = LoggingOptions.getDefaultInstance.getService;
    val logName = "alexr-test-log"
    val resource = MonitoredResource.newBuilder("global").build

    val entry: LogEntry = LogEntry
      .newBuilder(MyJsonPayload.payload)
      .setSeverity(Severity.EMERGENCY)
      .setLogName(logName)
      .setResource(resource)
      .build

    logging.write(entry)
    Thread.sleep(1000)
  }
}
