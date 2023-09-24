/** https://docs.scala-lang.org/overviews/compiler-options/ */
object ScalacOpts {

  /** default project encoding
    */
  val utf8: Seq[String] = Seq("-encoding", "UTF-8")

  /** Emit warning and location for usages of features that should be imported explicitly.
    */
  val feature = "-feature"

  /** Emit warning and location for usages of deprecated APIs.
    */
  val deprecation     = "-deprecation"
  val lintDeprecation = "-Xlint:deprecation"

  /** Enable additional warnings where generated code depends on assumptions.
    */
  val unchecked = "-unchecked"

  /** Allow postfix operator notation, such as 1 to 10 toList
    */
  val postfix = "-language:postfixOps"

  /** Allow higher-kinded types: F[_]
    */
  val higherKindedTypes = "-language:higherKinds"

  val macroAnnotations = "-Ymacro-annotations"

  /** treat warning as fatal */
  val warningsAsFatals = "-Xfatal-warnings"

  /** fine-grained 2.13.2+ control */
  val matchShouldBeExhaustive = "-Wconf:cat=other-match-analysis:error"
}
