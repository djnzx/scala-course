/**
  * https://docs.scala-lang.org/overviews/compiler-options/
  */
object ScalacOpts {

  /**
    * default project encoding
    */
  val encoding = "-encoding"
  val UTF8 = "UTF-8"
  /**
    * Emit warning and location for usages of features that should be imported explicitly.
    */
  val feature = "-feature"
  /**
    * Emit warning and location for usages of deprecated APIs.
    */
  val deprecation = "-deprecation"
  /**
    * Enable additional warnings where generated code depends on assumptions.
    */
  val unchecked = "-unchecked"
  /**
    * Allow postfix operator notation, such as 1 to 10 toList
    */
  val postfix = "-language:postfixOps"
  /**
    * Allow higher-kinded types: F[_]
    */
  val higherKindedTypes = "-language:higherKinds"

}
