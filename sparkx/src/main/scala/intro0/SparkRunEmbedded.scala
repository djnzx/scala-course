package intro0

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkRunEmbedded extends App {

  println(ScalaVersion.line)

  val spark = SparkSession.builder()
    .appName("Example #1")
    .master("local")
    .getOrCreate()

  val cars = getClass.getResource("cars.json").toURI.toString

  val df: DataFrame = spark.read
    .option("inferSchema", true)
    .json(cars)

  df.printSchema()

}
