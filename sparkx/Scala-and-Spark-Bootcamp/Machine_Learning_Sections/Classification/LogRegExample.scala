/////////////////////////////////////////////
/// SIMPLE VERSION OF LOG REG EXAMPLE //////
///////////////////////////////////////////

// Note that usually all imports would occur at the top and
// most of this would be in an object this layout if for learning purposes only

// Logistic Regression Example
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.SparkSession

// Optional: Use the following code below to set the Error reporting
import org.apache.log4j._
Logger.getLogger("org").setLevel(Level.ERROR)

// Spark Session
val spark = SparkSession.builder().getOrCreate()

// Use Spark to read in the Titanic csv file.
val data = spark.read.option("header","true").option("inferSchema","true").format("csv").load("titanic.csv")

// Print the Schema of the DataFrame
data.printSchema()

///////////////////////
/// Display Data /////
/////////////////////
val colnames = data.columns
val firstrow = data.head(1)(0)
println("\n")
println("Example Data Row")
for(ind <- Range(1,colnames.length)){
  println(colnames(ind))
  println(firstrow(ind))
  println("\n")
}

////////////////////////////////////////////////////
//// Setting Up DataFrame for Machine Learning ////
//////////////////////////////////////////////////

// Grab only the columns we want
val logregdataall = data.select(data("Survived").as("label"), $"Pclass", $"Sex", $"Age", $"SibSp", $"Parch", $"Fare", $"Embarked")
val logregdata = logregdataall.na.drop()

// A few things we need to do before Spark can accept the data!
// We need to deal with the Categorical columns


// Import VectorAssembler and Vectors
import org.apache.spark.ml.feature.{VectorAssembler,StringIndexer,VectorIndexer,OneHotEncoder}
import org.apache.spark.ml.linalg.Vectors

// Deal with Categorical Columns

val genderIndexer = new StringIndexer().setInputCol("Sex").setOutputCol("SexIndex")
val embarkIndexer = new StringIndexer().setInputCol("Embarked").setOutputCol("EmbarkIndex")

val genderEncoder = new OneHotEncoder().setInputCol("SexIndex").setOutputCol("SexVec")
val embarkEncoder = new OneHotEncoder().setInputCol("EmbarkIndex").setOutputCol("EmbarkVec")

// Assemble everything together to be ("label","features") format
val assembler = (new VectorAssembler()
                  .setInputCols(Array("Pclass", "SexVec", "Age","SibSp","Parch","Fare","EmbarkVec"))
                  .setOutputCol("features") )


////////////////////////////
/// Split the Data ////////
//////////////////////////
val Array(training, test) = logregdata.randomSplit(Array(0.7, 0.3), seed = 12345)


///////////////////////////////
// Set Up the Pipeline ///////
/////////////////////////////
import org.apache.spark.ml.Pipeline

val lr = new LogisticRegression()

val pipeline = new Pipeline().setStages(Array(genderIndexer,embarkIndexer,genderEncoder,embarkEncoder,assembler, lr))

// Fit the pipeline to training documents.
val model = pipeline.fit(training)

// Get Results on Test Set
val results = model.transform(test)

////////////////////////////////////
//// MODEL EVALUATION /////////////
//////////////////////////////////

// For Metrics and Evaluation
import org.apache.spark.mllib.evaluation.MulticlassMetrics

// Need to convert to RDD to use this
val predictionAndLabels = results.select($"prediction",$"label").as[(Double, Double)].rdd

// Instantiate metrics object
val metrics = new MulticlassMetrics(predictionAndLabels)

// Confusion matrix
println("Confusion matrix:")
println(metrics.confusionMatrix)
