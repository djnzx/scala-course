import ScalacOpts.*

/** https://docs.scala-lang.org/overviews/compiler-options/index.html
  * https://docs.scala-lang.org/overviews/compiler-options/index.html#Warning_Settings
  * https://blog.ssanj.net/posts/2019-06-14-scalac-2.13-options-and-flags.html
  * http://eed3si9n.com/stricter-scala-with-xlint-xfatal-warnings-and-scalafix
  */
object CompilerOptions {

  val javacOptions = Seq(
    "-source",
    "11",
    "-target",
    "11",
  )

  val scalacOptions = Seq(
    encoding,
    UTF8,
    feature,
    deprecation,
    unchecked,
    postfix,
    higherKindedTypes,
    macroAnnotations, // 2.13+, used by newtype
    warningsAsFatals, // ??? doesn't work as expected
    matchShouldBeExhaustive,
    lintDeprecation,
    // "-Xlint:unsound-match",
    // "-Ypartial-unification", // by default since 2.13
    "-language:existentials",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-dead-code",
    "-Ywarn-unused",
    "-Yrepl-class-based",
    "-Yrangepos",
    "-explaintypes",
    // doesn't work for 2.11
//    "-Ywarn-extra-implicit",
//    "-Xlint:unused,-type-parameter-shadow",
//    "-opt-warnings",
//    "-opt:l:inline",
//    "-opt-inline-from:<source>",
  )
}
