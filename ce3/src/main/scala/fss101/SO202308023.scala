package fs2x

// https://stackoverflow.com/questions/76821092/check-whether-a-key-exists-or-not-in-a-scala-map
object SO202308023 extends App {

  case class ConfigKey(repRole: String)

  class configInfo (
    var modelMin: String = "",
    var modelMax: String = "",
    var delMax: String = "",
    var delAdj: String = "",
    var scrAttr: String = "") extends Serializable {
    override def toString():String = {
      return modelMin+"-"+modelMax+"-"+delMax+"-"+delAdj+"-"+scrAttr
    }
  }

  type configType  = scala.collection.mutable.Map[ConfigKey, configInfo]

  val repRole: String = "CUST EXE"
  val modelMin: String = "420.0"
  val modelMax: String = "490.0"
  val delMax: String = "-20.0"
  val delAdj: String = "400.0"
  val scrAttr: String = "Zero_To_Many"

  var tempConfigMap: configType = scala.collection.mutable.Map()

  var confKey: ConfigKey = ConfigKey(repRole)
  var confInfo: configInfo = new configInfo()

  //confKey.repRole =
  confInfo.modelMin = modelMin
  confInfo.modelMax = modelMax
  confInfo.delMax = delMax
  confInfo.delAdj = delAdj
  confInfo.scrAttr = scrAttr

  if (tempConfigMap.contains(confKey)) {
    tempConfigMap(confKey) = confInfo
  }
  else {
    tempConfigMap += confKey -> confInfo
  }
  var AGENTROLE: String = "CUST EXE"
  var configKeys: ConfigKey = new ConfigKey(AGENTROLE)
  var configKeyDef: ConfigKey = new ConfigKey("Default")

  if(tempConfigMap.contains(configKeys))
    println("True")
  else
    println("False")

}
