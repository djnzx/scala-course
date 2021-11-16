import Dependencies.Libraries

import Versions._
import Dependencies.pf
import Dependencies.Libraries.CompilerPlugins
import sbt.Def.spaceDelimited
import sbt.Keys._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val commonSettings = Seq(
  scalaVersion := vScala,
  organization := "org.alexr",
  version := "21.11.06",
  javacOptions ++= CompilerOptions.javacOptions,
  // https://docs.scala-lang.org/overviews/compiler-options/index.html#Warning_Settings
  // http://eed3si9n.com/stricter-scala-with-xlint-xfatal-warnings-and-scalafix
  scalacOptions ++= CompilerOptions.scalacOptions,
  // temporary
  scalacOptions -= ScalacOpts.warningsAsFatals,
  resolvers ++= Resolvers.all,
  libraryDependencies ++=
    Seq(
      Libraries.pprint,
      Libraries.fansi,
    ) ++
      Libraries.scalaTest,
)

/** some algorithms implementations */
lazy val algorithms = (project in file("algorithms"))
  .settings(
    commonSettings,
  )

/** AMT WSDL experiments */
lazy val amt = (project in file("amt"))
  .settings(
    commonSettings,
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

/** Cats Effects 2.x */
lazy val ce2 = (project in file("ce2"))
  .settings(
    commonSettings,
    addCompilerPlugin(CompilerPlugins.kindProjector),
    libraryDependencies ++= {
      val vCatsTagless = "0.11"
      val vFs2 = "2.5.10"
      val vHttp4s = "0.21.4"
      val vCirce = "0.13.0"
      val vMunit = "0.7.8"
      val vLogback = "1.2.3"

      Seq(
        "org.typelevel" %% "cats-core"            % cats,
        "org.typelevel" %% "cats-effect"          % catsEffect2,
        "org.typelevel" %% "cats-effect-laws"     % catsEffect2,
        "co.fs2"        %% "fs2-core"             % vFs2,
        "co.fs2"        %% "fs2-reactive-streams" % vFs2,
        "io.circe"      %% "circe-generic"        % vCirce,
        "org.http4s"    %% "http4s-blaze-server"  % vHttp4s,
        "org.http4s"    %% "http4s-blaze-client"  % vHttp4s,
        "org.http4s"    %% "http4s-circe"         % vHttp4s,
        "org.http4s"    %% "http4s-dsl"           % vHttp4s,
        "org.typelevel" %% "cats-tagless-macros"  % vCatsTagless,
        "org.scalameta" %% "munit-scalacheck"     % vMunit,
        "org.typelevel" %% "munit-cats-effect-2"  % "1.0.6",
        "ch.qos.logback" % "logback-classic"      % vLogback,
      )
    },
  )

/** Cats Effects 3.x */
lazy val ce3 = (project in file("ce3"))
  .settings(
    commonSettings,
    addCompilerPlugin(CompilerPlugins.betterMonadicFor),
    addCompilerPlugin(CompilerPlugins.contextApplied),
//    addCompilerPlugin(CompilerPlugins.kindProjector),
    libraryDependencies ++= Seq(
      // CATS
      "org.typelevel" %% "cats-core"   % cats,
      "org.typelevel" %% "cats-effect" % catsEffect3,
      // FS2
      "co.fs2" %% "fs2-core"             % "3.1.5", // 3.2.9
      "co.fs2" %% "fs2-reactive-streams" % "3.1.5",
      // testing
      "org.typelevel" %% "munit-cats-effect-3" % "1.0.6",
    ),
  )

/** cognitOps experiments area */
lazy val co = (project in file("co"))
  .settings(
    commonSettings,
    scalacOptions -= "-Ymacro-annotations",
    scalaVersion := vScala212,
    libraryDependencies ++= Seq(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.contextApplied,
      CompilerPlugins.kindProjector,
      // Cats
      Libraries.cats,
      Libraries.catsLaws,
      Libraries.catsEffect,
      Libraries.catsMtlCore,
      "dev.profunktor" %% "console4cats" % "0.8.1",
      // fs2
      Libraries.fs2core,
      Libraries.fs2reactive,
      // http4s
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
      Libraries.sqlPg,
      Libraries.doobieCore,
      Libraries.doobiePg,
      Libraries.doobieHikari,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

/** ZIO v.1 */
lazy val zio1 = (project in file("zio1"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      pf.zio %% "zio"          % zio1v,
      pf.zio %% "zio-streams"  % zio1v,
      pf.zio %% "zio-test"     % zio1v,
      pf.zio %% "zio-test-sbt" % zio1v % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

/** ZIO v.2 */
lazy val zio2 = (project in file("zio2"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      pf.zio %% "zio"          % zio2v,
      pf.zio %% "zio-streams"  % zio2v,
      pf.zio %% "zio-test"     % zio2v,
      pf.zio %% "zio-test-sbt" % zio2v % Test,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
  )

/** Dotty experiments */
lazy val plain3 = (project in file("plain3"))
  .settings(
    scalaVersion := vScala31,
    description := "Example sbt project that compiles using Scala 3",
  )

lazy val s3fs = (project in file("s3fs"))
  .settings(
    scalaVersion := vScala31,
    libraryDependencies += "co.fs2" %% "fs2-core" % fs2ce3,
    initialCommands := s"""
    import fs2._, cats.effect._, cats.effect.unsafe.implicits.global
  """,
  )

/** FP in Scala (RED Book) Mostly plain Scala only a few libraries involved */
lazy val fp_red = (project in file("fp_red"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      Libraries.scalaCheck,
      Libraries.scalaTestPlus,
    ),
  )

/** http4s v1 */
lazy val http4s1 = (project in file("http4s1"))
  .settings(
    commonSettings, {
      def http4s(artifact: String) = "org.http4s" %% artifact % "1.0.0-M25"

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
      "com.google.cloud" % "google-cloud-logging" % "3.0.1",
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
      pf.lihaoyi                %% "upickle"    % "1.4.0", // http://www.lihaoyi.com/upickle
      pf.lihaoyi                %% "ujson"      % "1.4.0",
      pf.lihaoyi                %% "os-lib"     % "0.7.8", // https://github.com/lihaoyi/os-lib
      pf.lihaoyi                %% "scalatags"  % "0.9.4",
      pf.lihaoyi                %% "requests"   % "0.6.9",
      pf.lihaoyi                %% "geny"       % "0.6.10",
      pf.lihaoyi                %% "fastparse"  % "2.3.2", // https://www.lihaoyi.com/fastparse/
      "com.atlassian.commonmark" % "commonmark" % "0.15.0",
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
      Libraries.sqlPg,
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
      "org.scala-lang.modules"     %% "scala-parallel-collections" % "1.0.3",
      "com.softwaremill.quicklens" %% "quicklens"                  % "1.7.3",
      "org.scala-lang"              % "scala-reflect"              % vScala,
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
      "org.scala-lang"   % "scala-reflect"        % vScala,
      "com.github.fd4s" %% "fs2-kafka"            % "1.7.0",
      "com.google.cloud" % "google-cloud-logging" % "3.0.1",
    ),
  )

/** Lightbend (Typesafe) Stack:
  *
  * Akka, Akka-Streams, Akka-Http Play, Lagom, Slick
  *
  * https://www.lightbend.com
  */
lazy val typesafe = (project in file("typesafe"))
  .settings(
    commonSettings,
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

lazy val sparkx = (project in file("sparkx"))
  .settings(
    commonSettings,
    scalaVersion := vScala212,
//    scalaVersion := vScala211,
    scalacOptions -= ScalacOpts.macroAnnotations,
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.4.7",
      "org.apache.spark" %% "spark-sql"  % "2.4.7",
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
