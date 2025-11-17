import sbt.Def.*
import sbt.Keys.*
import scala.collection.immutable.Seq

object Settings {

  val common: Seq[Setting[?]] = Seq(
    organization := "alexr",
    version := "2025.11.17",
    resolvers ++= Resolvers.all,
    javacOptions ++= CompilerOptions.javacOptions,
  )

  val common2: Seq[Setting[?]] = common ++ Seq(
    scalaVersion := Versions.`2.13`,
    scalacOptions ++= CompilerOptions.scalacOptions,
    scalacOptions -= ScalacOpts.warningsAsFatals,
    libraryDependencies ++= CompilerPlugins.all,
    libraryDependencies ++= Libraries.testingToolkit2,
  )

  val common3: Seq[Setting[?]] = common ++ Seq(
    scalaVersion := Versions.`3.lts`,
    libraryDependencies ++= Libraries.testingToolkit3,
    scalacOptions ++= CompilerOptions.scalacOptions3,
  )

}
