import ScalacOpts._

object CompilerOptions {
  val scalac = Seq(
    encoding, UTF8,
    feature,
    deprecation,
    unchecked,
    postfix,
    higherKindedTypes,
    "-Xfatal-warnings",     // treat warning as fatal
    //"-Ypartial-unification", // by default since 2.13
    "-language:existentials",
    "-Ymacro-annotations",   // used by newtype
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
