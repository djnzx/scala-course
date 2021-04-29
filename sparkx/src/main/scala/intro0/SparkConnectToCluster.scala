package intro0

import org.apache.spark.sql.SparkSession

object SparkConnectToCluster extends App {

  println(ScalaVersion.line)

  val address = "spark://localhost:7077"

  val spark = SparkSession.builder()
    .appName("Example #2")
    .master(address)
    .getOrCreate()

  val data = (1 to 100).toList

  val df = spark
    .createDataFrame(data.map(Tuple1(_)))

  df.printSchema()

}
