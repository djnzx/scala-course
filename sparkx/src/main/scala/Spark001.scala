import org.apache.spark.sql.{Column, DataFrame, SparkSession}
import pprint.{pprintln => println}
/**
  * http://spark.apache.org/docs/latest/quick-start.html
  */
object Spark001 {
  /** attaching to engine */
  def instance() = SparkSession.builder()
    .appName("Example #1")
    .master("local")
    .getOrCreate()
  
  def a1(spark: SparkSession) = {
    /**
      * CSV, JSON, JDBC, TEXT, ORC, Parquet, AVRO
      * Untyped Operations
      */
    val df: DataFrame = spark.read.text("1.txt")
    df.printSchema()
    df.select("column").show()
    df.filter( _ => true)
    df.groupBy("col").count().show()
  }

  val cars = getClass.getResource("cars.json").toURI.toString
  def readFile(spark: SparkSession, name: String) = 
    spark.read
      //      .option("header", "true") // for CSV
      .option("inferSchema", true)
      .json(name)
 
  /** reading untyped dataframe locally */
  def readUntypedDataFrame(spark: SparkSession) = {
    val df = readFile(spark, cars)
    df.show(10)
    // by default all are the strings 
    for (line <- df.head(5)) {
      println(line)
    }
    val cs: Array[String] = df.columns
    df.describe().show() // summary on numeric data
    df.select("Miles_per_Gallon").show()
//    df.select($"Name", $"Miles_per_Gallon").show()
    val df2 = df.withColumn("acc2", df("Acceleration")+1)
    df2.printSchema()
    df2.show()
    val col1: Column = df2("acc2").as("A2")
    df2.select(col1).show()
  }
  
  def main(args: Array[String]): Unit = {
    val spark = instance()
    readUntypedDataFrame(spark)
  }
}
