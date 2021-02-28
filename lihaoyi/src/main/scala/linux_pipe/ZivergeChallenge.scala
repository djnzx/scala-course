package linux_pipe

import Domain._

import java.io.{BufferedReader, InputStreamReader}
import upickle.default._

import scala.util.Try

object ZivergeChallenge extends App {

  def readItem(s: String) = Try(read[Item](s)).toOption
  
  val cmd = Array("/bin/sh", "-c", "~/Downloads/blackbox")
  val process = Runtime.getRuntime.exec(cmd)
  val input = process.getInputStream
  val reader = new BufferedReader(new InputStreamReader(input))
  
  // TODO: 1 handle reader.close on break, register java hook
  // TODO: 2 collect data to the state, implement f: (State, Item) => State
  // TODO: 3 expose counter to a web-server
  
  def processIt: Unit = {
    readItem(reader.readLine()).foreach(println)
    processIt
  }
  
  processIt
}
