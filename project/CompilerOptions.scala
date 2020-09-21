import ScalacOpts._

/**
  * https://docs.scala-lang.org/overviews/compiler-options/index.html
  * https://blog.ssanj.net/posts/2019-06-14-scalac-2.13-options-and-flags.html
  */
object CompilerOptions {
  val scalac = Seq(
    encoding, UTF8,
    feature,
    deprecation,
    unchecked,
    postfix,
    higherKindedTypes,
    macroAnnotations,       // 2.13+, used by newtype
    warningsAsFatals,  // ??? doesn't work as expected
    //"-Ypartial-unification", // by default since 2.13
    "-language:existentials",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-dead-code",
    "-Ywarn-extra-implicit",
    "-Ywarn-unused",
    "-Xlint:unused,-type-parameter-shadow",
    "-Yrepl-class-based",
    "-Yrangepos",
    "-explaintypes",
    "-opt-warnings",
    "-opt:l:inline",
    "-opt-inline-from:<source>",
  )
}
