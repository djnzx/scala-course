// LOGISTIC REGRESSION CLASSIFIER
// DOCUMENTATION EXAMPLE

// $example on$
import org.apache.spark.ml.classification.LogisticRegression
// $example off$
import org.apache.spark.sql.SparkSession

object LogisticRegressionWithElasticNetExample {

  def main(): Unit = {
    val spark = SparkSession
      .builder
      .appName("LogisticRegressionWithElasticNetExample")
      .getOrCreate()

    // $example on$
    // Load training data
    val training = spark.read.format("libsvm").load("sample_libsvm_data.txt")

    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.3)
      .setElasticNetParam(0.8)

    // Fit the model
    val lrModel = lr.fit(training)

    // Print the coefficients and intercept for logistic regression
    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")
    // $example off$

    spark.stop()
  }
}
