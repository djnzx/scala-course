import Spark001.{cars, instance, readFile}
import org.apache.spark.sql.{Column, DataFrame, Row}
import org.apache.spark.sql.functions._

/**
  * dataFrame operations
  * https://sparkbyexamples.com/spark/spark-dataframe-where-filter/
  */
object Spark002 {

  def filterEx(df: DataFrame) = {
    /**
      * spark syntax vs scala syntax
      */
    val c: Column = df("Miles_per_Gallon")
    df.filter(c > 15).show()
    df.filter(c === 17).show()
    df.filter(c === 17 || c === 25).show()
    df.filter(c > 15 && c < 25).show()
    df.filter("Miles_per_Gallon > 15 and Miles_per_Gallon < 25")
    df.filter("Miles_per_Gallon = 17")
  }
  
  def collectAnd(df: DataFrame) = {
    val c: Column = df("Miles_per_Gallon")
    val collected: Array[Row] = df.filter(c === 17 || c === 25).collect()
    df.filter(c === 17 || c === 25).count()
  }
  
  def funcs(df: DataFrame) = {
    /**
      * a lot of stuff inside
      * org.apache.spark.sql.functions._
      */
    //    import spark.implicits._
    val c1a = df("Miles_per_Gallon")
    val c1b = df("Acceleration")
    //    val c3x: Column = corr(c1a, c1b)
    //    val c3y: Column = corr("Miles_per_Gallon", "Acceleration")
    //    val c1: Column = col("Miles_per_Gallon")
    //    val c2: Column = col("Acceleration")
//        val c3: Column = corr(c1, c2)
    //    df.select(c3).show()
    //    collected.foreach(println)
  }
  
  def main(args: Array[String]): Unit = {
    val spark = instance()
    val df = readFile(spark, cars)
    df.printSchema()
//    filterEx(df)
//    collectAnd(df)
    funcs(df)
  }
}
