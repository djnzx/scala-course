import sbt.Def.*
import sbt.Keys.*
import scala.collection.immutable.Seq

object Settings {

  val common: Seq[Setting[?]] = Seq(
    scalaVersion := Versions.vScala213,
    organization := "alexr",
    version := "2024.02.07",
    javacOptions ++= CompilerOptions.javacOptions,
    scalacOptions ++= CompilerOptions.scalacOptions,
    scalacOptions -= ScalacOpts.warningsAsFatals,
    resolvers ++= Resolvers.all,
    libraryDependencies ++= CompilerPlugins.all,
    libraryDependencies ++= Libraries.testingToolkit,
  )

}
