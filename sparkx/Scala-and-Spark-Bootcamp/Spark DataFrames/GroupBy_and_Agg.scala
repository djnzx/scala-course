// GROUP BY and AGG (Aggregate methods)

// Start a simple Spark Session
import org.apache.spark.sql.SparkSession
val spark = SparkSession.builder().getOrCreate()

// Create a DataFrame from Spark Session read csv
// Technically known as class Dataset
val df = spark.read.option("header","true").option("inferSchema","true").csv("Sales.csv")

// Show Schema
df.printSchema()

// Show
df.show()

// Groupby Categorical Columns
// Optional, usually won't save to another object
df.groupBy("Company")

// Mean
df.groupBy("Company").mean().show()
// Count
df.groupBy("Company").count().show()
// Max
df.groupBy("Company").max().show()
// Min
df.groupBy("Company").min().show()
// Sum
df.groupBy("Company").sum().show()

// Other Aggregate Functions
// http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.functions$
df.select(countDistinct("Sales")).show() //approxCountDistinct
df.select(sumDistinct("Sales")).show()
df.select(variance("Sales")).show()
df.select(stddev("Sales")).show() //avg,max,min,sum,stddev
df.select(collect_set("Sales")).show()

// OrderBy
// Ascending
df.orderBy("Sales").show()

// Descending
df.orderBy($"Sales".desc).show()
