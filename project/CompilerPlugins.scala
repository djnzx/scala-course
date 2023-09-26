import sbt.*

object CompilerPlugins {

  /** https://github.com/typelevel/kind-projector */
  private val kindProjector = compilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full)
  /** https://github.com/oleg-py/better-monadic-for */
  private val betterMonadicFor = compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
  /** https://github.com/augustjune/context-applied */
  private val contextApplied = compilerPlugin("org.augustjune" %% "context-applied" % "0.1.4")

  val all: Seq[ModuleID] = Seq(
    kindProjector,
    betterMonadicFor,
    contextApplied
  )

  // look for the library corresponding
  val silencer = compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.6.0" cross CrossVersion.full)

}
