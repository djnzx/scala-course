import Dependencies.{Libraries, Versions, pf}
import Dependencies.Libraries.CompilerPlugins
import sbt.Keys._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val commonSettings = Seq(
  scalaVersion := "2.13.3",
  organization := "org.alexr",
  version      := "20.08.18",

  javacOptions  ++= Seq(
    //  "-source", "1.8",
    //  "-target", "1.8"
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
//    CompilerPlugins.silencer,
//    Libraries.silencerAnnotation,
    Libraries.scalaTest,
  ),
)

lazy val whole = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "learn-scala",
  )
  .aggregate(
    scala_plain,
    fp_red,
    lihaoyi,
    typesafe,
    typelevel,
    mix,
    degoes,
    dotty
  )

/**
  * plain Scala
  * no 3rd-party libraries involved
  */
lazy val scala_plain = (project in file("scala_plain"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      Libraries.shapeless,
      "com.typesafe.play"          %% "play-json"                  % Versions.play,
      "org.scala-lang.modules"     %% "scala-parallel-collections" % "0.2.0",
      "com.softwaremill.quicklens" %% "quicklens"                  % "1.4.12",
      pf.lihaoyi         %% "pprint"       % "0.5.9",
      "org.scala-lang" % "scala-reflect" % "2.13.3",

      Libraries.scalaCheck,
      Libraries.scalaTestPlus,
      Libraries.scalactic,
    )
  )

/**
  * FP in Scala (RED Book)
  * Mostly plain Scala
  * only a few libraries involved
  */
lazy val fp_red = (project in file("fp_red"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      pf.lihaoyi         %% "pprint"       % "0.5.9",
      Libraries.scalaCheck,
      Libraries.scalaTestPlus,
      Libraries.scalactic,
    )
  )

/**
  * Project to investigate Li Haoyi libraries
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
      pf.lihaoyi         %% "os-lib"       % "0.7.0", // https://github.com/lihaoyi/os-lib
      pf.lihaoyi         %% "pprint"       % "0.5.9",
      pf.lihaoyi         %% "scalatags"    % "0.9.1",
      pf.lihaoyi         %% "geny"         % "0.6.0",
      pf.lihaoyi         %% "upickle"      % "1.1.0", // http://www.lihaoyi.com/upickle
      pf.lihaoyi         %% "fastparse"    % "2.2.2", // https://www.lihaoyi.com/fastparse/
      "com.atlassian.commonmark" % "commonmark"                % "0.15.0",
    )
  )

/**
  * Lightbend (Typesafe) Stack: Akka, Play, Slick, Lagom
  * https://www.lightbend.com
  */
lazy val typesafe = (project in file("typesafe"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      // untyped - old examples
      Libraries.akka("akka-actor"),
      // typed - current version
      Libraries.akka("akka-actor-typed"),
      // streams
      Libraries.akka("akka-stream"),
      // http
      Libraries.akkaHttp("akka-http"),
      // json
      Libraries.akkaHttp("akka-http-spray-json"),
      
      Libraries.slf4j("slf4j-simple"),
      // Slick
      Libraries.slickCore,
      Libraries.slickHikari,
      // config
      Libraries.tsconfig,
    )
  )

/**
  * Typelevel (FP) stack: Cats, Http4s, Ciris, Shapeless, Doobie, Fs2, Scalacheck, PureConfig
  * https://typelevel.org
  */
lazy val typelevel = (project in file("typelevel"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      // CATS
      Libraries.cats,
      Libraries.catsLaws,
      Libraries.catsEffect,
      Libraries.catsMtlCore,
    )
  )

/**
  * a lot of dirty, mixed code
  * Needs to be polished
  */
lazy val mix = (project in file("mix"))
  .settings(commonSettings)
  .settings(
    // https://alvinalexander.com/scala/sbt-how-specify-main-method-class-to-run-in-project
    mainClass in (Compile, run) := Some("degoes.fp_to_the_max.steps.StepG3"),

    libraryDependencies ++= Seq(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.contextApplied,
      CompilerPlugins.kindProjector,

      "ch.qos.logback"         % "logback-classic"             % "1.2.3",
      "io.lemonlabs"           %% "scala-uri"                  % "1.5.1",
      "io.getquill"            %% "quill-jdbc"                 % "3.5.1",
      "org.flywaydb"            %  "flyway-core"                 % "6.4.2",
      "dev.profunktor"         %% "console4cats"               % "0.8.1",
      // markdown parsing
      "com.atlassian.commonmark" % "commonmark"                % "0.15.0",

      Libraries.doobieCore,
      Libraries.doobiePg,

      Libraries.shapeless,
      Libraries.sqlPg,
      Libraries.sqlH2,

      // scalaZ
      Libraries.scalazCore,
      Libraries.scalazEffect,

      // FS2
      Libraries.fs2core,
      Libraries.fs2reactive,

      // CATS
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.catsMtlCore,

      // Serialization
      Libraries.circeCore,
      Libraries.circeGeneric,
      Libraries.circeParser,
      Libraries.circeRefined,

      // HTTP
      Libraries.http4sServer,
      Libraries.http4sDsl,
      Libraries.http4sClient,
      Libraries.http4sCirce,
      Libraries.http4sJwtAuth,

      Libraries.jsoup,

      Libraries.scalaCheck,
      Libraries.scalaTestPlus,
      Libraries.scalactic,

      // @newtype annotation
      Libraries.newtype,
      // refined types
      Libraries.refinedCore
    ),
  )

/**
  * John A. De Goes Project ZIO
  * https://zio.dev
  * https://ziverge.com
  */
lazy val degoes = (project in file("degoes"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.lemonlabs"           %% "scala-uri"                  % "1.5.1",
      // ZIO
      Libraries.zio("zio"),
      Libraries.zio("zio-test"),
      Libraries.zio("zio-test-sbt"),
      Libraries.zio("zio-test-magnolia"),
      Libraries.zio("zio-streams"),
      Libraries.doobieCore,
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )

lazy val dotty = (project in file("dotty"))
  .settings(
    scalaVersion := "0.26.0-RC1"
  )
  .settings(
    libraryDependencies ++= Seq(
    )
  )
//initialCommands in console := "import scalaz._, Scalaz._"
//addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
//addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
// https://www.scala-sbt.org/release/docs/Library-Dependencies.html
