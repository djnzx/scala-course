import java.lang

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.functions._

/**
  * http://localhost:4040
  */
object AppBroadcastSession {
  
  val spark: SparkSession = SparkSession.builder()
    .appName("Broadcast Joins")
    .master("local")
    .getOrCreate()
  
  // huge
  val table: Dataset[lang.Long] = spark.range(1, 10000000) // column "id"
  
  // small
  val rows: RDD[Row] = spark.sparkContext.parallelize(List(
    Row(1, "Gold"),
    Row(2, "Silver"),
    Row(3, "Bronze"),
  ))
  
  val rowsSchema: StructType = StructType(Array(
    StructField("id", IntegerType),
    StructField("metal", StringType),
  ))
  
  val lookupTable: DataFrame = spark.createDataFrame(rows, rowsSchema)
  
  val joined: DataFrame = table.join(lookupTable, "id")
  /**
    * Exchange hashpartitioning(id#0L, 200), true, [id=#27]
    * is an EXPENSIVE operation
    */
  joined.explain()

//  val joinedSmart = table.join(broadcast(lookupTable), "id")
//  joinedSmart.explain()
  
  def main(args: Array[String]): Unit = {
    Thread.sleep(10000000)
  }
  
}
