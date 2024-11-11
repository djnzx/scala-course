package djnz

import org.apache.spark.sql.SparkSession

trait SparkSessionWrapper extends Serializable {

  lazy val spark = SparkSession.builder()
    .appName("Example #1")
    .master("local")
    .getOrCreate()

}
