import Dependencies.Libraries
import Dependencies.Libraries.CompilerPlugins
import ScalacOpts._
import sbt.Keys._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val commonSettings = Seq(
  scalaVersion := "2.13.2",
  organization := "org.alexr",
  version      := "20.5.28",

  javacOptions  ++= Seq(
    //  "-source", "1.8",
    //  "-target", "1.8"
  ),

  // https://docs.scala-lang.org/overviews/compiler-options/index.html#Warning_Settings
  // http://eed3si9n.com/stricter-scala-with-xlint-xfatal-warnings-and-scalafix
  scalacOptions ++= Seq(
    encoding, UTF8,
    feature,
    deprecation,
    unchecked,
    postfix,
    higherKindedTypes,
    "-Xfatal-warnings",     // treat warning as fatal
    //"-Ypartial-unification", // by default since 2.13
    "-language:existentials",
    "-Ymacro-annotations",   // used by newtype
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Ywarn-dead-code",
    "-Ywarn-extra-implicit",
    "-Ywarn-unused",
    "-Xlint:unused,-type-parameter-shadow",
    "-Yrepl-class-based",
    "-Yrangepos",
    "-explaintypes",
    "-opt-warnings",
    "-opt:l:inline",
    "-opt-inline-from:<source>",
  ),

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
    CompilerPlugins.silencer,
    Libraries.silencerAnnotation,
  ),
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "learn-scala",

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

      // ZIO
      Libraries.zioCore,
      Libraries.zioStreams,

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
      Libraries.scalaTest,
      Libraries.scalaTestPlus,
      Libraries.scalactic,

      // @newtype annotation
      Libraries.newtype,
      // refined types
      Libraries.refinedCore
    ),
  )
  .aggregate(scala_plain, fp_red, lihaoyi, typesafe, typelevel)

lazy val scala_plain = (project in file("scala_plain"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.play"          %% "play-json"                  % "2.8.1",
      "org.scala-lang.modules"     %% "scala-parallel-collections" % "0.2.0",
      "com.softwaremill.quicklens" %% "quicklens"                  % "1.4.12",
      Libraries.shapeless,
    )
  )

lazy val fp_red = (project in file("fp_red"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      Libraries.scalaCheck,
    )
  )

lazy val lihaoyi = (project in file("lihaoyi"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi"            %% "os-lib"                     % "0.7.0",
      "com.lihaoyi"            %% "pprint"                     % "0.5.9",
      "com.lihaoyi"            %% "scalatags"                  % "0.9.1",
      "com.lihaoyi"            %% "geny"                       % "0.6.0",
      "com.lihaoyi"            %% "upickle"                    % "1.1.0",
    )
  )

lazy val typesafe = (project in file("typesafe"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      // untyped - old examples
      Libraries.akka("akka-actor"),
      // typed - current version
      Libraries.akka("akka-actor-typed"),
      Libraries.slf4j("slf4j-simple"),
      // Slick
      Libraries.slickCore,
      Libraries.slickHikari,
      // config
      Libraries.tsconfig,
    )
  )

lazy val typelevel = (project in file("typelevel"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
    )
  )

//initialCommands in console := "import scalaz._, Scalaz._"
//addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
//addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
// https://www.scala-sbt.org/release/docs/Library-Dependencies.html
