package linux_pipe

import Domain._

import java.io.{BufferedReader, InputStreamReader}
import upickle.default._

import java.time.Instant
import scala.util.Try

object ZivergeChallenge extends App {

  val frameSize = 20 // in seconds
  
  def readItem(s: String) = Try(read[Item](s)).toOption
  
  val cmd = Array("/bin/sh", "-c", "~/Downloads/blackbox")
  val runtime = Runtime.getRuntime
  val process = runtime.exec(cmd)
  val input = process.getInputStream
  val reader = new BufferedReader(new InputStreamReader(input))

  // TODO: expose Frame to a web-server
  
  def processIt(frame: Frame): Unit = {
    val raw = reader.readLine()
    val item = readItem(raw)
    val newFrame: Frame = item.map(frame.combine).getOrElse(frame)
    item.foreach { _ => println(newFrame) }
    processIt(newFrame)
  }
  
  val now = Instant.now().getEpochSecond
  processIt(Frame.next(now, frameSize))
}
