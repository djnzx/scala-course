package catsx.c114reader

import cats.data.Reader

object Reader7ConfigReading extends App {
  
  case class Config(port: Int, threshold: Double)
  
  def getPort = Reader { c: Config => c.port }
  def getThreshold: Reader[Config, Double] = Reader { _.threshold }
  
  def getPair: Reader[Config, (Int, Double)] =
    for {
      port      <- getPort
      threshold <- getThreshold
    } yield (port, threshold)
    
  val (port, threshold) = getPair(Config(12, -13.78))
}
