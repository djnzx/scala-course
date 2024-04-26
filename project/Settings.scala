import sbt.Def.*
import sbt.Keys.*
import scala.collection.immutable.Seq

object Settings {

  val common: Seq[Setting[?]] = Seq(
    organization := "alexr",
    version := "2024.04.27",
    resolvers ++= Resolvers.all,
    javacOptions ++= CompilerOptions.javacOptions,
  )

  val common2: Seq[Setting[?]] = common ++ Seq(
    scalaVersion := Versions.vScala213,
    scalacOptions ++= CompilerOptions.scalacOptions,
    scalacOptions -= ScalacOpts.warningsAsFatals,
    libraryDependencies ++= CompilerPlugins.all,
    libraryDependencies ++= Libraries.testingToolkit2,
  )

  val common3: Seq[Setting[?]] = common ++ Seq(
    scalaVersion := Versions.vScala3,
    libraryDependencies ++= Libraries.testingToolkit3,
  )

}
