import sbt.Def.spaceDelimited
import sbt.Keys._
import sbtbuildinfo.BuildInfoOption

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val v = Versions

lazy val commonSettings = Seq(
  scalaVersion := v.vScala,
  organization := "alexr",
  version := "2023.03.03",
  javacOptions ++= CompilerOptions.javacOptions,
  scalacOptions ++= CompilerOptions.scalacOptions,
  scalacOptions -= ScalacOpts.warningsAsFatals,
  resolvers ++= Resolvers.all,
  libraryDependencies ++= Seq(
    Libraries.pprint,
    Libraries.fansi,
    Libraries.sourcecode,
    Libraries.scalaCheck,
    Libraries.scalaTestFunSpec,
    Libraries.scalaTestShould,
    Libraries.scalaTestScalaCheckIntegration
  )
)

lazy val compilerPlugins = Seq(
  libraryDependencies ++= Seq(
    CompilerPlugins.kindProjector,
    CompilerPlugins.contextApplied,
    CompilerPlugins.betterMonadicFor
  )
)

lazy val avro101 = (project in file("avro101"))
  .settings(
    scalaVersion := v.vScala212,
    javacOptions ++= CompilerOptions.javacOptions,
    scalacOptions ++= CompilerOptions.scalacOptions,
    scalacOptions -= ScalacOpts.warningsAsFatals, // we are learning, there is no sense to be so strict
    scalacOptions -= ScalacOpts.macroAnnotations, // 2.12 doesn't have it
    libraryDependencies ++= Seq(
      Libraries.pprint,
      Libraries.fansi,
      Libraries.sourcecode,
      Libraries.scalaCheck,
      Libraries.scalaTestFunSpec,
      Libraries.scalaTestShould,
      Libraries.scalaTestScalaCheckIntegration,
      "org.apache.avro"   % "avro"                % "1.11.0",
      "org.apache.kafka" %% "kafka-streams-scala" % "3.3.1",
      Libraries.cats
    )
  )

lazy val mono101 = (project in file("mono101"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      Libraries.pprint,
      "dev.optics" %% "monocle-core"  % "3.2.0",
      "dev.optics" %% "monocle-macro" % "3.2.0"
    )
  )

lazy val munitx = (project in file("munitx"))
  .settings(
    commonSettings,
    libraryDependencies --= Seq(
      Libraries.scalaCheck,
      Libraries.scalaTestFunSpec,
      Libraries.scalaTestShould,
      Libraries.scalaTestScalaCheckIntegration
    ),
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit"            % "1.0.0-M7",
      "org.scalameta" %% "munit-scalacheck" % "1.0.0-M7"
    )
  )

lazy val algorithms = (project in file("algorithms"))
  .settings(
    commonSettings
  )

lazy val amt = (project in file("amt"))
  .settings(
    commonSettings,
    description := "AMT WSDL experiments",
    libraryDependencies ++= Seq(
      "org.apache.axis"   % "axis"              % "1.4",   // no transitive
      "org.apache.axis"   % "axis-saaj"         % "1.4",   // no transitive
      "org.apache.axis"   % "axis-jaxrpc"       % "1.4",   // no transitive
      "axis"              % "axis-wsdl4j"       % "1.5.1", // no transitive
      // "wsdl4j" % "wsdl4j" % "1.6.3", // no transitive
      // just to avoid warnings in runtime
      "commons-discovery" % "commons-discovery" % "0.5",   // transitive: commons-logging" % "commons-logging" % "1.1.1"
      "javax.mail"        % "mail"              % "1.4.7"  // transitive: "javax.activation" % "activation" % "1.1",
    )
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
      "org.typelevel"   %% "cats-core"            % v.cats,
      "org.typelevel"   %% "cats-free"            % v.cats,
      "org.typelevel"   %% "cats-effect"          % v.catsEffect2,
      "org.typelevel"   %% "cats-effect-laws"     % v.catsEffect2,
      "co.fs2"          %% "fs2-core"             % v.fs2ce2,
      "co.fs2"          %% "fs2-reactive-streams" % v.fs2ce2,
      "co.fs2"          %% "fs2-io"               % v.fs2ce2,
      "com.github.fd4s" %% "fs2-kafka"            % "1.10.0", // CE2
      Libraries.circeParser,    // plain parsers to Map => "circe-core", "circe-jawn" => "circe-numbers"
      Libraries.circeGenericEx, // generic derivation, adt support => "circe-generic" => "circe-core"
      Libraries.circeShapes,
      Libraries.circeTesting,
//      Libraries.circeRefined,
      "org.http4s"          %% "http4s-blaze-server"  % v.http4sCe2,
      "org.http4s"          %% "http4s-blaze-client"  % v.http4sCe2,
      "org.http4s"          %% "http4s-ember-server"  % v.http4sCe2,
      "org.http4s"          %% "http4s-ember-client"  % v.http4sCe2,
      "org.http4s"          %% "http4s-circe"         % v.http4sCe2,
      "org.http4s"          %% "http4s-dsl"           % v.http4sCe2,
      Libraries.doobieCore,
      Libraries.doobiePg,
      Libraries.sqlPgDriver,
      "dev.profunktor"      %% "console4cats"         % "0.8.1",
      "org.scala-lang"       % "scala-reflect"        % v.vScala,
      "com.google.cloud"     % "google-cloud-logging" % "3.7.2",
      "org.typelevel"       %% "cats-tagless-macros"  % "0.11",
      "org.scalameta"       %% "munit-scalacheck"     % "0.7.8",
      "org.typelevel"       %% "munit-cats-effect-2"  % "1.0.6",
      "ch.qos.logback"       % "logback-classic"      % v.logback,
      "com.kubukoz"         %% "debug-utils"          % "1.1.3",
      "org.mongodb.scala"   %% "mongo-scala-driver"   % "4.5.1",
      "com.beachape"        %% "enumeratum"           % "1.7.0",
      "com.beachape"        %% "enumeratum-circe"     % "1.7.0",
      "com.beachape"        %% "enumeratum-doobie"    % "1.7.0",
      "com.sksamuel.avro4s" %% "avro4s-core"          % "4.1.0",
      "com.sksamuel.avro4s" %% "avro4s-json"          % "4.1.0",
      "com.sksamuel.avro4s" %% "avro4s-kafka"         % "4.1.0",
      "com.sksamuel.avro4s" %% "avro4s-refined"       % "4.1.0",
      Libraries.scalaTestFunSpec,
      Libraries.scalaTestShould,
      Libraries.catsLaws,    // ?
      Libraries.catsMtlCore, // ?
      Libraries.newtype,
      Libraries.refinedCore,
      Libraries.shapeless
    ),
    dependencyOverrides ++= Seq(
      "io.circe" %% "circe-core" % "0.14.4"
    )
  )

lazy val ce3 = (project in file("ce3"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    commonSettings,
    buildInfoPackage := "alexr",
    buildInfoOptions ++= Seq(BuildInfoOption.BuildTime, BuildInfoOption.ToMap),
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion /*, libraryDependencies */ ),
    description := "Cats Effects 3",
    libraryDependencies ++= Seq(
      CompilerPlugins.kindProjector,
      CompilerPlugins.contextApplied,
      CompilerPlugins.betterMonadicFor,
      "org.typelevel"               %% "cats-core"             % v.cats,
      "org.typelevel"               %% "cats-effect"           % v.catsEffect3,
      "com.github.cb372"            %% "cats-retry"            % "3.1.0",
      "co.fs2"                      %% "fs2-core"              % v.fs2ce3,
      "co.fs2"                      %% "fs2-io"                % v.fs2ce3,
      "org.typelevel"               %% "munit-cats-effect-3"   % "1.0.7",
      "org.http4s"                  %% "http4s-dsl"            % v.http4sCe3, // transitive: "http4s-core"
      "org.http4s"                  %% "http4s-circe"          % v.http4sCe3,
      "org.http4s"                  %% "http4s-blaze-server"   % v.http4sCe3,
      "org.http4s"                  %% "http4s-blaze-client"   % v.http4sCe3,
      "org.http4s"                  %% "http4s-ember-server"   % v.http4sCe3,
      "org.http4s"                  %% "http4s-ember-client"   % v.http4sCe3,
      "com.softwaremill.sttp.tapir" %% "tapir-core"            % "1.2.9",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"      % "1.2.9",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"   % "1.2.9",
      "org.typelevel"               %% "log4cats-core"         % "2.5.0",
      "org.typelevel"               %% "log4cats-slf4j"        % "2.5.0",
      "io.circe"                    %% "circe-parser"          % "0.15.0-M1",
      "io.circe"                    %% "circe-optics"          % "0.14.1",
      "io.circe"                    %% "circe-generic-extras"  % v.circeGenericExtras,
      "io.circe"                    %% "circe-yaml"            % v.circeYaml,
      "io.circe"                    %% "circe-fs2"             % "0.14.1",
      "com.github.fd4s"             %% "fs2-kafka"             % "2.5.0",
      "com.beachape"                %% "enumeratum"            % "1.7.2",
      "com.beachape"                %% "enumeratum-circe"      % "1.7.2",
      "com.beachape"                %% "enumeratum-doobie"     % "1.7.3",
      "com.beachape"                %% "enumeratum-cats"       % "1.7.2",
      "com.beachape"                %% "enumeratum-scalacheck" % "1.7.2",
      "io.kubernetes"                % "client-java-api"       % "17.0.1",
      "io.kubernetes"                % "client-java"           % "17.0.1",
      "jakarta.mail"                 % "jakarta.mail-api"      % "2.1.1",
      "io.scalaland"                %% "chimney"               % "0.7.0"
    )
  )
  .enablePlugins(ScalaxbPlugin)
  .settings(
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
    )
  )

lazy val httpt = (project in file("httpt"))
  .settings(
    commonSettings,
    compilerPlugins,
    description := "HTTP load tests",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl"          % "1.0.0-M39",
      "org.http4s" %% "http4s-circe"        % "1.0.0-M39",
      "org.http4s" %% "http4s-ember-server" % "1.0.0-M39",
      "org.http4s" %% "http4s-blaze-server" % "1.0.0-M38",
      "org.http4s" %% "http4s-jetty-server" % "1.0.0-M32"
    ),
    dependencyOverrides ++= Seq(
      "org.http4s" %% "http4s-core"   % "1.0.0-M39",
      "org.http4s" %% "http4s-server" % "1.0.0-M39"
    )
  )

// sbt k8a/docker:publishLocal
// docker images | grep k8a
lazy val k8a = (project in file("k8a"))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    dockerBaseImage := "openjdk:11-jre-slim",
    dockerExposedPorts := Seq(8080),
    dockerGroupLayers := {
      case (_, path) if path.startsWith("alexr") => 2
      case _                                     => 1
    },
    description := "artifact to experiment with k8",
    commonSettings,
    libraryDependencies ++= Seq(
      CompilerPlugins.kindProjector,
      CompilerPlugins.contextApplied,
      CompilerPlugins.betterMonadicFor,
      "com.github.cb372" %% "cats-retry"           % "3.1.0",
      "org.http4s"       %% "http4s-dsl"           % "1.0.0-M36",
      "org.http4s"       %% "http4s-blaze-server"  % "1.0.0-M36",
      "org.http4s"       %% "http4s-circe"         % "1.0.0-M36",
      "io.circe"         %% "circe-generic-extras" % "0.14.2"
//      "io.circe"         %% "circe-parser"         % "0.15.0-M1",
    )
  )

/** John A. De Goes Project ZIO https://zio.dev https://ziverge.com */
lazy val degoes = (project in file("degoes"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "io.lemonlabs"          %% "scala-uri"        % "1.5.1",
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
      Libraries.doobieHikari
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val fp_red = (project in file("fp_red"))
  .settings(
    commonSettings,
    description := "FP in Scala (RED Book) Mostly plain Scala only a few libraries involved",
    libraryDependencies ++= Seq(
      Libraries.scalaTestFunSpec,
      Libraries.scalaTestShould,
      Libraries.scalaCheck,
      Libraries.scalaTestScalaCheckIntegration
    )
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
      pf.lihaoyi                %% "upickle"    % "2.0.0", // http://www.lihaoyi.com/upickle
      pf.lihaoyi                %% "ujson"      % "2.0.0",
      pf.lihaoyi                %% "os-lib"     % "0.8.1", // https://github.com/lihaoyi/os-lib
      pf.lihaoyi                %% "scalatags"  % "0.11.1",
      pf.lihaoyi                %% "requests"   % "0.7.1",
      pf.lihaoyi                %% "geny"       % "0.7.1",
      pf.lihaoyi                %% "fastparse"  % "2.3.3", // https://www.lihaoyi.com/fastparse/
      "com.atlassian.commonmark" % "commonmark" % "0.17.0"
    )
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
      "org.typelevel"    %% "simulacrum"                 % "1.0.1",
      "org.scalaz"       %% "scalaz-core"                % "7.3.7",
      "com.propensive"   %% "contextual"                 % "1.2.1",
      Libraries.refinedCore,
      Libraries.refinedScalaz,
      "org.scalaz"       %% "scalaz-deriving-jsonformat" % "2.0.0-M5",
      "org.apache.lucene" % "lucene-core"                % "7.1.0", // 9.4.0
      "org.apache.lucene" % "lucene-queryparser"         % "7.1.0",
      "org.apache.lucene" % "lucene-analyzers-common"    % "7.1.0",
      "org.apache.lucene" % "lucene-memory"              % "7.1.0"
    )
  )

/** protobuf experiments */
lazy val pbx = (project in file("pbx"))
  .enablePlugins(ScalaxbPlugin)
  .settings(
    commonSettings,
    Compile / PB.targets := Seq(
      scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
    )
  )

lazy val plain2 = (project in file("plain2"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      CompilerPlugins.kindProjector,
      "io.chymyst"                 %% "curryhoward"                % "0.3.8",
      "org.scala-lang.modules"     %% "scala-parallel-collections" % "1.0.3",
      "com.softwaremill.quicklens" %% "quicklens"                  % "1.7.3",
      "org.scala-lang"              % "scala-reflect"              % v.vScala,
      "org.scalaxb"                %% "scalaxb"                    % "1.8.0",
      "io.vavr"                     % "vavr"                       % "1.0.0-alpha-3",
      Libraries.scalaTestWhole,
      Libraries.scalaCheck,
      Libraries.scalaTestScalaCheckIntegration
    )
  )

lazy val plain3 = (project in file("plain3"))
  .settings(
    scalaVersion := v.vScala31,
    description := "Example sbt project that compiles using Scala 3"
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
      "org.apache.spark" %% "spark-core" % "3.2.1", // 2.12 / 2.13
      "org.apache.spark" %% "spark-sql"  % "3.2.1"  // 2.12 / 2.13
    )
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
      s"${pf.typesafe}.play" %% "play-json"       % v.play,
      // Slick
      Libraries.slickCore,
      Libraries.slickHikari,
      // config
      Libraries.tsconfig,
      // logger
      Libraries.slf4j("slf4j-simple"),
      "ch.qos.logback"        % "logback-classic" % "1.2.3"
    )
  )

lazy val zio1 = (project in file("zio1"))
  .settings(
    commonSettings,
    description := "ZIO v1",
    libraryDependencies ++= Seq(
      pf.zio %% "zio"          % v.zio1v,
      pf.zio %% "zio-streams"  % v.zio1v,
      pf.zio %% "zio-test"     % v.zio1v,
      pf.zio %% "zio-test-sbt" % v.zio1v % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val zio2 = (project in file("zio2"))
  .settings(
    commonSettings,
    description := "ZIO v2",
    libraryDependencies ++= Seq(
      pf.zio %% "zio"          % v.zio2v,
      pf.zio %% "zio-streams"  % v.zio2v,
      pf.zio %% "zio-test"     % v.zio2v,
      pf.zio %% "zio-test-sbt" % v.zio2v % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

//addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
//addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
// https://www.scala-sbt.org/release/docs/Library-Dependencies.html

lazy val initCommands = s"""
    import cats._, cats.data._, cats.implicits._, fs2._, cats.effect._, cats.effect.unsafe.implicits.global
  """

lazy val ping = inputKey[Unit]("Will ping the server")
ping := {
  println("pinging server...")
  val x: Seq[String] = spaceDelimited("<arg>").parsed
  println(x)
}
