// Start a simple Spark Session
import org.apache.spark.sql.SparkSession
val spark = SparkSession.builder().getOrCreate()

// Grab small dataset with some missing data
val df = spark.read.option("header","true").option("inferSchema","true").csv("ContainsNull.csv")

// Show schema
df.printSchema()

// Notice the missing values!
df.show()

// You basically have 3 options with Null values
// 1. Just keep them, maybe only let a certain percentage through
// 2. Drop them
// 3. Fill them in with some other value
// No "correct" answer, you'll have to adjust to the data!

// Dropping values
// Technically still experimental, but it has been around since 1.3

// Drop any rows with any amount of na values
df.na.drop().show()

// Drop any rows that have less than a minimum Number
// of NON-null values ( < Int)
df.na.drop(2).show()

// Interesting behavior!
// What happens when using double/int versus strings

// Fill in the Na values with Int
df.na.fill(100).show()

// Fill in String will only go to all string columns
df.na.fill("Emp Name Missing").show()

// Be more specific, pass an array of string column names
df.na.fill("Specific",Array("Name")).show()

// There are also more complicated map capabilities!

// Exercise: Fill in Sales with average sales.
// How to get averages? For now, a simple way is to use describe!
df.describe().show()

// Now fill in with the values
df.na.fill(400.5).show()

// It would be nice to be able to just directly
// use aggregate functions. So let's learn about them
// with groupby and agg up next!
