import Dependencies.Libraries
import Dependencies.Versions
import Dependencies.pf
import Dependencies.Libraries.CompilerPlugins
import sbt.Def.spaceDelimited
import sbt.Keys._

lazy val vScala = "2.13.6"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val commonSettings = Seq(
  scalaVersion := vScala,
  organization := "org.alexr",
  version := "21.08.04",
  javacOptions ++= Seq(
//    "-source",
//    "11",
//    "-target",
//    "11",
  ),
  // https://docs.scala-lang.org/overviews/compiler-options/index.html#Warning_Settings
  // http://eed3si9n.com/stricter-scala-with-xlint-xfatal-warnings-and-scalafix
  scalacOptions ++= CompilerOptions.scalac,
  scalacOptions --= Seq(
    "-Xfatal-warnings",
  ),
  resolvers ++= Seq(
    Resolver.mavenLocal,
    Resolver.mavenCentral,
//    Resolver.typesafeRepo("releases"),
//    Resolver.sonatypeRepo("releases"),
//    Resolver.sonatypeRepo("snapshots"),
    Repos.artima,
  ),
  libraryDependencies ++= Seq(
//    Libraries.scalaTest,
    Libraries.scalaTestFunSpec,
    Libraries.scalaTestShould,
    Libraries.pprint,
    Libraries.fansi,
  ),
)

/** protobuf experiments */
lazy val pbx = (project in file("pbx"))
  .settings(commonSettings)
  .settings(
  )

/** google cloud logging experiments */
lazy val lcx = (project in file("lcx"))
  .settings(commonSettings)

lazy val plain2 = (project in file("plain2"))
  .settings(commonSettings)

lazy val scala_plain = (project in file("scala_plain"))
  .enablePlugins(ScalaxbPlugin)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.3",
      "com.softwaremill.quicklens" %% "quicklens" % "1.7.3",
      "org.scala-lang" % "scala-reflect" % vScala,
      "org.scalaxb" %% "scalaxb" % "1.8.0",
      Libraries.scalaCheck,
      Libraries.scalaTestPlus,
      Libraries.scalactic,
//      "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
    ),
  )

/** some algorithms
  * implementations
  */
lazy val algorithms = (project in file("algorithms"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      Libraries.scalactic,
    ),
  )

/** FP in Scala (RED Book)
  * Mostly plain Scala
  * only a few libraries involved
  */
lazy val fp_red = (project in file("fp_red"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      Libraries.scalaCheck,
      Libraries.scalaTestPlus,
      Libraries.scalactic,
    ),
  )

/** Project to investigate Li Haoyi libraries
  * https://www.lihaoyi.com
  * https://www.handsonscala.com
  * https://github.com/lihaoyi
  *
  * +request
  * +ujson: upickle vs ujson
  * +api_macros
  */
lazy val lihaoyi = (project in file("lihaoyi"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      pf.lihaoyi %% "upickle" % "1.4.0", // http://www.lihaoyi.com/upickle
      pf.lihaoyi %% "ujson" % "1.4.0",
      pf.lihaoyi %% "os-lib" % "0.7.8", // https://github.com/lihaoyi/os-lib
      pf.lihaoyi %% "scalatags" % "0.9.4",
      pf.lihaoyi %% "requests" % "0.6.9",
      pf.lihaoyi %% "geny" % "0.6.10",
      pf.lihaoyi %% "fastparse" % "2.3.2", // https://www.lihaoyi.com/fastparse/
      "com.atlassian.commonmark" % "commonmark" % "0.15.0",
    ),
  )

/** Lightbend (Typesafe) Stack:
  *
  * Akka,
  * Akka-Streams,
  * Akka-Http
  * Play,
  * Lagom,
  * Slick
  *
  * https://www.lightbend.com
  */
lazy val typesafe = (project in file("typesafe"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      // untyped - old examples
      Libraries.akka("akka-actor"),
      Libraries.akka("akka-actor-typed"),
      Libraries.akka("akka-stream"),
      Libraries.akkaHttp("akka-http"),
      Libraries.akkaHttp("akka-http-spray-json"),
      // play JSON
      s"${pf.typesafe}.play" %% "play-json" % Versions.play,
      // Slick
      Libraries.slickCore,
      Libraries.slickHikari,
      // config
      Libraries.tsconfig,
      // logger
      Libraries.slf4j("slf4j-simple"),
      "ch.qos.logback" % "logback-classic" % "1.2.3",
    ),
  )

lazy val typelevel = (project in file("typelevel"))
  .settings(commonSettings)

lazy val co = (project in file("co"))
  .settings(commonSettings)
  .settings(
    scalacOptions -= "-Ymacro-annotations",
//    scalaVersion := "2.12.13",
    libraryDependencies ++= Seq(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.contextApplied,
      CompilerPlugins.kindProjector,
      // CATS
      Libraries.cats,
      Libraries.catsLaws,
      Libraries.catsEffect,
      Libraries.catsMtlCore,
      "dev.profunktor" %% "console4cats" % "0.8.1",
      // FS2
      Libraries.fs2core,
      Libraries.fs2reactive,
      // HTTP
      Libraries.http4sServer,
      Libraries.http4sDsl,
      Libraries.http4sClient,
      Libraries.http4sCirce,
      Libraries.http4sJwtAuth,
      // Serialization
      Libraries.circeCore,
      Libraries.circeGeneric,
      Libraries.circeGenericEx,
      Libraries.circeParser,
      Libraries.circeRefined,
      // @newtype annotation
      Libraries.newtype,
      // refined types
      Libraries.refinedCore,
      // shapeless
      Libraries.shapeless,
//      "com.cognitops.common" %% "common-json"  % "8.13-SNAPSHOT",
//      "com.cognitops.common" %% "common-utils" % "8.13-SNAPSHOT",
    ),
  )

/** a lot of dirty, mixed code
  * Needs to be polished
  */
lazy val mix = (project in file("mix"))
  .settings(commonSettings)
  .settings(
    // https://alvinalexander.com/scala/sbt-how-specify-main-method-class-to-run-in-project
    libraryDependencies ++= Seq(
      CompilerPlugins.kindProjector,
      CompilerPlugins.betterMonadicFor,
      "io.getquill" %% "quill-jdbc" % "3.5.1",
      "org.flywaydb" % "flyway-core" % "6.4.2",
      Libraries.http4sServer, // URI
      Libraries.sqlPg,
      Libraries.jsoup,
//      "org.typelevel" %% "simulacrum" % "1.0.0",
      "com.github.mpilquist" %% "simulacrum" % "0.19.0",
      "org.scalaz" %% "scalaz-core" % "7.3.2",
      "com.propensive" %% "contextual" % "1.2.1",
      Libraries.refinedCore,
      Libraries.refinedScalaz,
      "org.scalaz" %% "scalaz-deriving-jsonformat" % "2.0.0-M5",
    ),
  )

lazy val sparkx = (project in file("sparkx"))
  .settings(commonSettings)

val zio1v = "1.0.10"
lazy val zio1 = (project in file("zio1"))
  .settings(
    commonSettings,
  )
  .settings(
    libraryDependencies ++= Seq(
      pf.zio %% "zio" % zio1v,
      pf.zio %% "zio-streams" % zio1v,
      pf.zio %% "zio-test" % zio1v,
      pf.zio %% "zio-test-sbt" % zio1v % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

val zio2v = "2.0.0-M1"
lazy val zio2 = (project in file("zio2"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      pf.zio %% "zio" % zio2v,
      pf.zio %% "zio-streams" % zio2v,
      pf.zio %% "zio-test" % zio2v,
      pf.zio %% "zio-test-sbt" % zio2v % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

/** John A. De Goes Project ZIO
  * https://zio.dev
  * https://ziverge.com
  */
lazy val degoes = (project in file("degoes"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.lemonlabs" %% "scala-uri" % "1.5.1",
      // ZIO
      Libraries.zio("zio"),
      Libraries.zio("zio-test"),
      Libraries.zio("zio-test-sbt"),
      Libraries.zio("zio-test-magnolia"),
      Libraries.zio("zio-streams"),
      pf.zio %% "zio-interop-cats" % "2.2.0.1",
      pf.zio %% "zio-kafka" % "0.13.0",
      "com.github.pureconfig" %% "pureconfig" % "0.14.0",
      Libraries.sqlPg,
      Libraries.doobieCore,
      Libraries.doobiePg,
      Libraries.doobieHikari,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

/** Dotty experiments
  */
lazy val dotty = (project in file("dotty"))
//initialCommands in console := "import scalaz._, Scalaz._"
//addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
//addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
// https://www.scala-sbt.org/release/docs/Library-Dependencies.html

lazy val ping = inputKey[Unit]("Will ping the server")
ping := {
  println("pinging server...")
  val x: Seq[String] = spaceDelimited("<arg>").parsed
  println(x)
}
