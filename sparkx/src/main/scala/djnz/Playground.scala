package djnz

import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.scalatest.Succeeded
import org.scalatest.funsuite.AnyFunSuite

// --add-exports java.base/sun.nio.ch=ALL-UNNAMED
class Playground extends AnyFunSuite with SparkSessionWrapper {

  import spark.implicits._

  test("0") {
    Succeeded
  }

  test("1") {

    val fileName = "cars.json"
    val path = this.getClass.getClassLoader.getResource(fileName).getFile

    pprint.log(path)

    // type DataFrame = Dataset[Row]
    val df0: DataFrame = spark.read
      .option("inferSchema", true)
      .json(path)

    df0.printSchema()

    val df = Seq(
      ("Boston", "USA", 0.67),
      ("Dubai", "UAE", 3.1),
      ("Cordoba", "Argentina", 1.39)
    ).toDF("city", "country", "population")

    df.printSchema()

    df.show()

    val df3 = df.withColumn("is_big_city", col("population") > 1)
    df3.show()

    df.filter(col("population") > 1)
      .show()

    val filteredDF = df.filter(col("population") > 1)
    filteredDF.show()

    val schema = df3.schema
    pprint.log(schema)

    val schema2 = StructType(Seq(
      StructField("first_name", StringType, nullable = true),
      StructField("age", IntegerType, nullable = true),
    ))
    pprint.log(schema2)

    val animalData = Seq(
      Row(30, "bat"),
      Row(2, "mouse"),
      Row(25, "horse"),
    )

    val animalSchema = List(
      StructField("avg_lifespan", IntegerType),
      StructField("animal_type", StringType),
    )

    val animalDF = spark.createDataFrame(
      spark.sparkContext.parallelize(animalData),
      StructType(animalSchema)
    )

    animalDF.show()

    animalDF.printSchema()

  }

  test("2") {
    val path = this.getClass.getClassLoader.getResource("file1.csv").getFile
    val df = spark.read
      .option("header", "true")
      .csv(path)

    df.show()
    df.printSchema()

    df
      .withColumn("speak", lit("meow"))
      .write
      .csv("/Users/alexr/dev/pp/scala-course/sparkx/output1_catalogue")
  }

  def sumColumns(c1: Column, c2: Column) =
    c1 + c2

  test("3") {
    val numbersDF = Seq((10, 4), (3, 4), (8, 4)).toDF("some_num", "another_num")
    numbersDF
      .withColumn(
        "the_sum",
        sumColumns(col("some_num"), col("another_num"))
      )
      .show()
  }

  def myConcat(word1: String)(word2: String): String = word1 + word2

  test("4") {

    def withCat(name: String)(df: DataFrame): DataFrame =
      df.withColumn("cat", lit(s"$name meow"))

    val stuffDF: DataFrame = Seq("chair", "hair", "bear").toDF("thing")

    val fn: DataFrame => DataFrame = withCat("darla")

    val df2 = stuffDF
      .transform(fn)
      .show()

  }

  object Weird {
    def hi() = "Welcome!"

    def trimUpper(col: Column) = trim(upper(col))

    def withScary()(df: DataFrame) = df.withColumn("scary", lit("boom!"))
  }

  test("5") {
    Seq("niCE", " CaR", "BAR ")
      .toDF("word")
      .withColumn("trim_upper_word", Weird.trimUpper(col("word")))
      .transform(Weird.withScary())
      .show()
  }

  // https://github.com/mrpowers-io/spark-daria/tree/main
  // https://github.com/databricks/spark-redshift

  test("6") {
    val df = Seq(
      ("thor", "new york"),
      ("aquaman", "atlantis"),
      ("wolverine", "new york")
    ).toDF("superhero", "city")

    df
      .withColumn("city_starts_with_new", $"city".startsWith("new"))
      .show()
  }

  test("7 - columns") {
    val c1: Column = $"city"

    val df = Seq(
      ("thor", "new york"),
      ("aquaman", "atlantis"),
      ("wolverine", "new york")
    ).toDF("superhero", "city")

    df
      .withColumn("city_starts_with_new", $"city".startsWith("new"))
      .show()

    val c2: Column = df("city")

    val c3: Column = col("city")

  }

  test("8") {
    val df = Seq((10, "cat"), (4, "dog"), (7, null)).toDF("num", "word")

    df
      .withColumn("num_gt_5", col("num").gt(5))
      .show()

    df
      .withColumn("word_first_two", col("word").substr(0, 2))
      .show()

    df
      .withColumn("num_plus_five", col("num").+(5))
      .show()

    df
      .withColumn("two_divided_by_num", lit(2) / col("num"))
      .show()

    df
      .withColumn("word_is_null", col("word").isNull)
      .show()

    df
      .where(col("word").isNotNull)
      .show()

  }

  test("9") {

    val df = Seq(("bat", "bat"), ("snake", "rat"), ("cup", "phone"), ("key", null)).toDF("word1", "word2")

    val c: Column =
      when($"word1" === $"word2", "same words")
        .when(length($"word1") > length($"word2"), "word1 is longer")
        .otherwise("i am confused")

    df
      .withColumn("word_comparison", c).show()

  }

  test("10") {
    Seq(2, 3, 4)
      .toDF("number")
      .withColumn("number_factorial", factorial(col("number")))
      .show()

    Seq(2, 3, 4)
      .toDF("number")
      .withColumn("number_factorial", factorial($"number"))
      .show()

    Seq("sophia", "sol", "perro").toDF("word")
      .withColumn("spanish_hi", lit("hola"))
      .show()

    Seq("china", "canada", "italy", "tralfamadore").toDF("word")
      .withColumn(
        "continent",
        when(col("word") === lit("china"), lit("asia"))
          .when(col("word") === lit("canada"), lit("north america"))
          .when(col("word") === lit("italy"), lit("europe"))
          .otherwise("not sure")
      ).show()

    Seq(10, 15, 25).toDF("age")
      .withColumn(
        "life_stage",
        when(col("age") < 13, "child")
          .when(col("age") >= 13 && col("age") <= 18, "teenager")
          .when(col("age") > 18, "adult")
      ).show()

    def lifeStage(col: Column): Column =
      when(col < 13, "child")
        .when(col >= 13 && col <= 18, "teenager")
        .when(col > 18, "adult")

    Seq(10, 15, 25).toDF("age")
      .withColumn("life_stage", lifeStage(col("age")))
      .show()

    def trimUpper(c: Column) = trim(upper(c))

    Seq(
      " some stuff",
      "like CHEESE "
    ).toDF("weird")
      .withColumn(
        "cleaned",
        trimUpper(col("weird"))
      )
      .show()

    // https://jaceklaskowski.gitbooks.io/mastering-spark-sql/content/spark-sql-udfs-blackbox.html

    /** UDF not performant */

    def lowerRemoveAllWhitespace(s: String): String =
      s.toLowerCase().replaceAll("\\s", "")

    val lowerRemoveAllWhitespaceUDF = udf[String, String](lowerRemoveAllWhitespace)

    Seq(
      "    some stuff",
      "like CHEESE    "
    ).toDF("weird")
      .select(
        lowerRemoveAllWhitespaceUDF(col("weird"))
          .as("cleaned")
      )

    def betterLowerRemoveAllWhitespace(s: String): Option[String] = {
      val str = Option(s).getOrElse(return None)
      Some(str.toLowerCase().replaceAll("\\s", ""))
    }

    val betterLowerRemoveAllWhitespaceUDF = udf[Option[String], String](betterLowerRemoveAllWhitespace)
    val df13 = List(
      "BOO ",
      " HOO ",
      null
    ).toDF("cry")

    val df14 = df13.select(
      betterLowerRemoveAllWhitespaceUDF(col("cry"))
        .as("cleaned")
    )

    df14.explain(true)
    df14.show()

    def bestLowerRemoveAllWhitespace()(col: Column): Column =
      lower(regexp_replace(col, "\\s+", ""))

    val df15 = df13.select(
      bestLowerRemoveAllWhitespace()(col("cry"))
        .as("cleaned")
    )
    df15.explain(true)
    df15.show()

    Seq("a ", "b ", " c", null).toDF("word")
      .withColumn("trimmed_word", trim(col("word")))

    def singleSpace(col: Column): Column =
      trim(regexp_replace(col, " +", " "))

    def removeAllWhitespace(col: Column): Column =
      regexp_replace(col, "\\s+", "")

    // group
    // join

  }

}
