import Dependencies.Libraries
import Dependencies.Libraries.CompilerPlugins
import ScalacOpts._
import sbt.Keys._

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val commonSettings = Seq(
  scalaVersion := "2.13.2",
  organization := "org.alexr",
  version      := "20.5.25",

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
    name := "learn-scala-deeper",

    // https://alvinalexander.com/scala/sbt-how-specify-main-method-class-to-run-in-project
    mainClass in (Compile, run) := Some("degoes.fp_to_the_max.steps.StepG3"),

    libraryDependencies ++= Seq(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.contextApplied,
      CompilerPlugins.kindProjector,

      "ch.qos.logback"         % "logback-classic"             % "1.2.3",
      "org.tpolecat"           %% "doobie-core"                % "0.8.8",
      "org.tpolecat"           %% "doobie-postgres"            % "0.8.8",
      "io.lemonlabs"           %% "scala-uri"                  % "1.5.1",
      "com.typesafe.akka"      %% "akka-actor-typed"           % "2.6.3",

      "io.getquill"            %% "quill-jdbc"                 % "3.5.1",
      "org.flywaydb"            %  "flyway-core"                 % "6.4.2",
      "dev.profunktor"         %% "console4cats"               % "0.8.1",
      // just investigation
      "com.lihaoyi"            %% "os-lib"                     % "0.7.0",
      "com.lihaoyi"            %% "pprint"                     % "0.5.9",
      "com.lihaoyi"            %% "scalatags"                  % "0.9.1",
      "com.lihaoyi"            %% "geny"                       % "0.6.0",
      "com.lihaoyi"            %% "upickle"                    % "1.1.0",
      // md parsing
      "com.atlassian.commonmark" % "commonmark"                % "0.15.0",

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

      // Slick
      Libraries.slickCore,
      Libraries.slickHikari,

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
  .aggregate(scala_plain, fp_red)

lazy val scala_plain = (project in file("scala_plain"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.play"          %% "play-json"                  % "2.8.1",
      "com.softwaremill.quicklens" %% "quicklens"                  % "1.4.12",
      "org.scala-lang.modules"     %% "scala-parallel-collections" % "0.2.0",
      "com.chuusai"                %% "shapeless"                  % "2.3.3",
      "com.typesafe"               %  "config"                      % "1.4.0",
    )
  )

lazy val fp_red = (project in file("fp_red"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      Libraries.scalaCheck,
    )
  )

//initialCommands in console := "import scalaz._, Scalaz._"
//addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
//addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
// https://www.scala-sbt.org/release/docs/Library-Dependencies.html
