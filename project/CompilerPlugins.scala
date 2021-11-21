import sbt._

object CompilerPlugins {

  /** https://github.com/typelevel/kind-projector */
  val kindProjector = compilerPlugin(pf.typelevel %% "kind-projector" % Versions.kindProjector cross CrossVersion.full)

  val betterMonadicFor = compilerPlugin("com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor)
  val contextApplied = compilerPlugin("org.augustjune" %% "context-applied" % Versions.contextApplied)
  val silencer = compilerPlugin("com.github.ghik" % "silencer-plugin" % Versions.silencer cross CrossVersion.full)

}
