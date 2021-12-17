import sbt.Keys._

import sbt.Def.spaceDelimited

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val v = Versions

lazy val commonSettings = Seq(
  scalaVersion := v.vScala,
  organization := "org.alexr",
  version := "21.11.21",
  javacOptions ++= CompilerOptions.javacOptions,
  scalacOptions ++= CompilerOptions.scalacOptions,
  scalacOptions -= ScalacOpts.warningsAsFatals,
  resolvers ++= Resolvers.all,
  libraryDependencies ++=
    Seq(
      Libraries.pprint,
      Libraries.fansi,
    ) ++
      Libraries.scalaTest,
)

lazy val algorithms = (project in file("algorithms"))
  .settings(
    commonSettings,
  )

lazy val amt = (project in file("amt"))
  .settings(
    commonSettings,
    description := "AMT WSDL experiments",
    libraryDependencies ++= Seq(
      "org.apache.axis" % "axis"        % "1.4", // no transitive
      "org.apache.axis" % "axis-saaj"   % "1.4", // no transitive
      "org.apache.axis" % "axis-jaxrpc" % "1.4", // no transitive
      "axis"            % "axis-wsdl4j" % "1.5.1", // no transitive
      // "wsdl4j" % "wsdl4j" % "1.6.3", // no transitive
      // just to avoid warnings in runtime
      "commons-discovery" % "commons-discovery" % "0.5", // transitive: commons-logging" % "commons-logging" % "1.1.1"
      "javax.mail"        % "mail"              % "1.4.7", // transitive: "javax.activation" % "activation" % "1.1",
    ),
  )

lazy val ce2 = project
  .in(file("ce2"))
  .settings(
    commonSettings,
//    evictionErrorLevel := util.Level.Warn,
    description := "Cats Effects 2",
    libraryDependencies ++= Seq(
      CompilerPlugins.kindProjector,
      CompilerPlugins.contextApplied,
      CompilerPlugins.betterMonadicFor,
      "org.typelevel" %% "cats-core"              % v.cats,
      "org.typelevel" %% "cats-free"              % v.cats,
      "org.typelevel" %% "cats-effect"            % v.catsEffect2,
      "org.typelevel" %% "cats-effect-laws"       % v.catsEffect2,
      "co.fs2"        %% "fs2-core"               % v.fs2ce2,
      "co.fs2"        %% "fs2-reactive-streams"   % v.fs2ce2,
      "io.circe"      %% "circe-generic"          % v.circe,
      "org.http4s"    %% "http4s-blaze-server"    % v.http4sCe2,
      "org.http4s"    %% "http4s-blaze-client"    % v.http4sCe2,
      "org.http4s"    %% "http4s-circe"           % v.http4sCe2,
      "org.http4s"    %% "http4s-dsl"             % v.http4sCe2,
      "org.http4s"    %% "http4s-jdk-http-client" % "0.3.7",
      Libraries.doobieCore,
      Libraries.doobiePg,
      Libraries.sqlPgDriver,
      "org.typelevel" %% "cats-tagless-macros" % "0.11",
      "org.scalameta" %% "munit-scalacheck"    % "0.7.8",
      "org.typelevel" %% "munit-cats-effect-2" % "1.0.6",
      "ch.qos.logback" % "logback-classic"     % v.logback,
      "com.kubukoz"   %% "debug-utils"         % "1.1.3",
    ),
  )

lazy val ce3 = (project in file("ce3"))
  .settings(
    description := "Cats Effects 3",
    commonSettings,
    libraryDependencies ++= Seq(
      CompilerPlugins.kindProjector,
      CompilerPlugins.contextApplied,
      CompilerPlugins.betterMonadicFor,
      "org.typelevel"               %% "cats-core"            % v.cats,
      "org.typelevel"               %% "cats-effect"          % v.catsEffect3,
      "co.fs2"                      %% "fs2-core"             % v.fs2ce3,
      "co.fs2"                      %% "fs2-reactive-streams" % v.fs2ce3,
      "org.typelevel"               %% "munit-cats-effect-3"  % "1.0.6",
      "org.http4s"                  %% "http4s-blaze-server"  % v.http4sCe3,
      "org.http4s"                  %% "http4s-blaze-client"  % v.http4sCe3,
      "org.http4s"                  %% "http4s-circe"         % v.http4sCe3,
      "org.http4s"                  %% "http4s-dsl"           % v.http4sCe3,
      "com.softwaremill.sttp.tapir" %% "tapir-core"           % "0.19.1",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"     % "0.19.1",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"  % "0.19.1",
    ),
  )

/** cognitOps experiments area */
lazy val co = (project in file("co"))
  .settings(
    commonSettings,
    scalacOptions -= "-Ymacro-annotations",
    scalaVersion := v.vScala212,
    libraryDependencies ++= Seq(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.contextApplied,
      CompilerPlugins.kindProjector,
      "org.http4s" %% "http4s-blaze-server"  % "0.21.31",
      "org.http4s" %% "http4s-dsl"           % "0.21.31",
      "org.http4s" %% "http4s-circe"         % "0.21.31",
      "org.http4s" %% "http4s-blaze-client"  % "0.21.31",
      "io.circe"   %% "circe-core"           % "0.13.0",
      "io.circe"   %% "circe-parser"         % "0.13.0",
      "io.circe"   %% "circe-generic"        % "0.13.0",
      "io.circe"   %% "circe-generic-extras" % "0.13.0",
    ),
  )

/** John A. De Goes Project ZIO https://zio.dev https://ziverge.com */
lazy val degoes = (project in file("degoes"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "io.lemonlabs" %% "scala-uri" % "1.5.1",
      // ZIO
      Libraries.zio1("zio"),
      Libraries.zio1("zio-test"),
      Libraries.zio1("zio-test-sbt"),
      Libraries.zio1("zio-test-magnolia"),
      Libraries.zio1("zio-streams"),
      pf.zio                  %% "zio-interop-cats" % "2.2.0.1",
      pf.zio                  %% "zio-kafka"        % "0.13.0",
      "com.github.pureconfig" %% "pureconfig"       % "0.14.0",
      Libraries.sqlPgDriver,
      Libraries.doobieCore,
      Libraries.doobiePg,
      Libraries.doobieHikari,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

lazy val zio1 = (project in file("zio1"))
  .settings(
    commonSettings,
    description := "ZIO v1",
    libraryDependencies ++= Seq(
      pf.zio %% "zio"          % v.zio1v,
      pf.zio %% "zio-streams"  % v.zio1v,
      pf.zio %% "zio-test"     % v.zio1v,
      pf.zio %% "zio-test-sbt" % v.zio1v % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

lazy val zio2 = (project in file("zio2"))
  .settings(
    commonSettings,
    description := "ZIO v2",
    libraryDependencies ++= Seq(
      pf.zio %% "zio"          % v.zio2v,
      pf.zio %% "zio-streams"  % v.zio2v,
      pf.zio %% "zio-test"     % v.zio2v,
      pf.zio %% "zio-test-sbt" % v.zio2v % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

lazy val plain3 = (project in file("plain3"))
  .settings(
    scalaVersion := v.vScala31,
    description := "Example sbt project that compiles using Scala 3",
  )

lazy val s3fs = (project in file("s3fs"))
  .settings(
    scalaVersion := v.vScala31,
    libraryDependencies += "co.fs2" %% "fs2-core" % v.fs2ce3,
    initialCommands := s"""
    import fs2._, cats.effect._, cats.effect.unsafe.implicits.global
  """,
  )

lazy val fp_red = (project in file("fp_red"))
  .settings(
    commonSettings,
    description := "FP in Scala (RED Book) Mostly plain Scala only a few libraries involved",
    libraryDependencies ++= Seq(
      Libraries.scalaCheck,
      Libraries.scalaTestPlus,
    ),
  )

/** http4s v1 */
lazy val http4s1 = (project in file("http4s1"))
  .settings(
    commonSettings, {
      def http4s(artifact: String) = "org.http4s" %% artifact % v.http4s1

      libraryDependencies ++= Seq(
        http4s("http4s-core"),
        http4s("http4s-dsl"),
        http4s("http4s-blaze-server"),
        http4s("http4s-circe"),
      )
    },
  )

/** google cloud logging */
lazy val lcx = (project in file("lcx"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.google.cloud" % "google-cloud-logging" % "3.5.1",
    ),
  )

/** Project to investigate Li Haoyi libraries https://www.lihaoyi.com https://www.handsonscala.com
  * https://github.com/lihaoyi
  *
  * +request +ujson: upickle vs ujson +api_macros
  */
lazy val lihaoyi = (project in file("lihaoyi"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      pf.lihaoyi                %% "upickle"    % "1.4.3", // http://www.lihaoyi.com/upickle
      pf.lihaoyi                %% "ujson"      % "1.4.3",
      pf.lihaoyi                %% "os-lib"     % "0.8.0", // https://github.com/lihaoyi/os-lib
      pf.lihaoyi                %% "scalatags"  % "0.11.0",
      pf.lihaoyi                %% "requests"   % "0.7.0",
      pf.lihaoyi                %% "geny"       % "0.7.0",
      pf.lihaoyi                %% "fastparse"  % "2.2.3", // https://www.lihaoyi.com/fastparse/
      "com.atlassian.commonmark" % "commonmark" % "0.17.0",
    ),
  )

/** a lot of dirty, mixed code Needs to be polished */
lazy val mix = (project in file("mix"))
  .settings(
    commonSettings, // https://alvinalexander.com/scala/sbt-how-specify-main-method-class-to-run-in-project
    libraryDependencies ++= Seq(
      CompilerPlugins.kindProjector,
      CompilerPlugins.betterMonadicFor,
      "io.getquill" %% "quill-jdbc"  % "3.5.1",
      "org.flywaydb" % "flyway-core" % "6.4.2",
      Libraries.http4sServer, // URI
      Libraries.sqlPgDriver,
      Libraries.jsoup,
      //      "org.typelevel" %% "simulacrum" % "1.0.0",
      "com.github.mpilquist" %% "simulacrum"  % "0.19.0",
      "org.scalaz"           %% "scalaz-core" % "7.3.2",
      "com.propensive"       %% "contextual"  % "1.2.1",
      Libraries.refinedCore,
      Libraries.refinedScalaz,
      "org.scalaz" %% "scalaz-deriving-jsonformat" % "2.0.0-M5",
    ),
  )

/** protobuf experiments */
lazy val pbx = (project in file("pbx"))
  .enablePlugins(ScalaxbPlugin)
  .settings(
    commonSettings,
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value / "scalapb",
    ),
  )

lazy val plain2 = (project in file("plain2"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      CompilerPlugins.kindProjector,
      "org.scala-lang.modules"     %% "scala-parallel-collections" % "1.0.3",
      "com.softwaremill.quicklens" %% "quicklens"                  % "1.7.3",
      "org.scala-lang"              % "scala-reflect"              % v.vScala,
      "org.scalaxb"                %% "scalaxb"                    % "1.8.0",
      "io.vavr"                     % "vavr"                       % "1.0.0-alpha-3",
      Libraries.scalaTestWhole,
      Libraries.scalaCheck,
      Libraries.scalaTestPlus,
//      "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
    ),
  )

/** typelevel stack */
lazy val typelevel = (project in file("typelevel"))
  .settings(
    commonSettings,
    /** treat non-exhaustive matches as fatal */
    scalacOptions ++= Seq(
      ScalacOpts.matchShouldBeExhaustive,
      ScalacOpts.lintDeprecation,
    ),
    libraryDependencies ++= Seq(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.contextApplied,
      CompilerPlugins.kindProjector,
      Libraries.cats,
      Libraries.catsLaws,
      Libraries.catsEffect,
      Libraries.catsMtlCore,
      "dev.profunktor" %% "console4cats" % "0.8.1",
      Libraries.fs2core,
      Libraries.fs2reactive,
      "com.github.fd4s" %% "fs2-kafka" % "1.7.0",
      Libraries.http4sServer,
      Libraries.http4sDsl,
      Libraries.http4sClient,
      Libraries.http4sCirce,
      Libraries.http4sJwtAuth,
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
      "org.scala-lang"   % "scala-reflect"        % v.vScala,
      "com.google.cloud" % "google-cloud-logging" % "3.0.1",
    ),
  )

lazy val typesafe = (project in file("typesafe"))
  .settings(
    commonSettings,
    description := "Lightbend (Typesafe) Stack: Akka, Akka-Streams, Akka-Http Play, Lagom, Slick (https://www.lightbend.com)",
    libraryDependencies ++= Seq(
      // untyped - old examples
      Libraries.akka("akka-actor"),
      Libraries.akka("akka-actor-typed"),
      Libraries.akka("akka-stream"),
      Libraries.akkaHttp("akka-http"),
      Libraries.akkaHttp("akka-http-spray-json"),
      // play JSON
      s"${pf.typesafe}.play" %% "play-json" % v.play,
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

lazy val sparkx = (project in file("sparkx"))
  .settings(
    commonSettings,
    scalaVersion := v.vScala213,
//    scalaVersion := v.scala212,
//    scalaVersion := v.scala211,
    scalacOptions -= ScalacOpts.macroAnnotations,
    libraryDependencies ++= Seq(
//      "org.apache.spark" %% "spark-core" % "2.4.7", // 2.11 / 2.12
//      "org.apache.spark" %% "spark-sql"  % "2.4.7", // 2.11 / 2.12
      "org.apache.spark" %% "spark-core" % "3.2.0", // 2.12 / 2.13
      "org.apache.spark" %% "spark-sql"  % "3.2.0", // 2.12 / 2.13
    ),
  )

//addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
//addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
// https://www.scala-sbt.org/release/docs/Library-Dependencies.html

lazy val ping = inputKey[Unit]("Will ping the server")
ping := {
  println("pinging server...")
  val x: Seq[String] = spaceDelimited("<arg>").parsed
  println(x)
}
