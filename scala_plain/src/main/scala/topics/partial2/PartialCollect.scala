package topics.partial2

import scala.util.matching.Regex

/**
  * example
  * how to combine:
  * regex, partial function and .collect
  * to one line string to map processing
  */
object PartialCollect {
  
  val opt: Regex = """(\S+)=(\S+)""".r
  
  def stringToMap(origin: String): Map[String, String] = {
    origin.split("\\s+").collect { case opt(a, b) => a -> b }.toMap
  }

  def stringToMapSI(origin: String): Map[String, Int] = {
    origin
      .split("\\s+")
      .collect { case opt(k, v) => k -> v }
      .flatMap { case (k, v) => v.toIntOption.map { k -> _ } }
      .toMap
  }

}
