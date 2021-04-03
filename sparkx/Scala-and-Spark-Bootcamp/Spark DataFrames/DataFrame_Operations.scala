// DataFrame Operations

// Start a simple Spark Session
import org.apache.spark.sql.SparkSession
val spark = SparkSession.builder().getOrCreate()

// Create a DataFrame from Spark Session read csv
// Technically known as class Dataset
val df = spark.read.option("header","true").option("inferSchema","true").csv("CitiGroup2006_2008")

// Show Schema
df.printSchema()

// Show head
// df.head(5)

//////////////////////////
//// FILTERING DATA //////
//////////////////////////
// This import is needed to use the $-notation
import spark.implicits._

// Grabbing all rows where a column meets a condition
df.filter($"Close" > 480).show()

// Can also use SQL notation
//df.filter("Close > 480").show()

// Count how many results
df.filter($"Close">480).count()

// Can also use SQL notation
// df.filter("Close > 480").count()

// Multiple Filters
// Note the use of triple === , this may change in the future!
df.filter($"High"===484.40).show()
// Can also use SQL notation
// df.filter("High = 484.40").count()

df.filter($"Close"<480 && $"High"<480).show()
// Can also use SQL notation
// df.filter("Close<480 AND High < 484.40").show()

// Collect results into a scala object (Array)
val High484 = df.filter($"High"===484.40).collect()

// Operations and Useful Functions
// http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.functions$

// Examples of Operations
df.select(corr("High","Low")).show() // Pearson Correlation
