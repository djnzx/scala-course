import Dependencies.Libraries
import Dependencies.Libraries.CompilerPlugins
import ScalacOpts._

Global / onChangedBuildSource := ReloadOnSourceChanges

name         := "learn-scala-deeper"
version      := "20.5.19"
scalaVersion := "2.13.2"

// https://alvinalexander.com/scala/sbt-how-specify-main-method-class-to-run-in-project
// sbt compile
// sbt run
mainClass in (Compile, run) := Some("degoes.fp_to_the_max.steps.StepG3")

javacOptions ++= Seq(
//  "-source", "1.8",
//  "-target", "1.8"
)
scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  feature,
  deprecation,
  unchecked,
  postfix,
  higherKindedTypes,
//  "-Xfatal-warnings",     // treat warning as fatal
//  "-Ypartial-unification", // by default since 2.13
  "-language:existentials",
  "-Ymacro-annotations", // used by newtype
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Yrepl-class-based",
  "-Ywarn-extra-implicit",
  "-Xlint:unused,-type-parameter-shadow",
  "-Yrangepos",
  "-explaintypes",
  // https://docs.scala-lang.org/overviews/compiler-options/index.html#Warning_Settings
  // http://eed3si9n.com/stricter-scala-with-xlint-xfatal-warnings-and-scalafix
  "-opt-warnings",
  "-opt:l:inline",
  "-opt-inline-from:<source>",
)
scalacOptions --= Seq(
  "-Xfatal-warnings",
  //  "-Ywarn-unused",
  //  "-Ywarn-dead-code",
)
//addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")
//addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
//addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.mavenCentral,
  Resolver.typesafeRepo("releases"),
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Repos.artima,
)

// https://www.scala-sbt.org/release/docs/Library-Dependencies.html
libraryDependencies ++= Seq(
  CompilerPlugins.betterMonadicFor,
  CompilerPlugins.contextApplied,
  CompilerPlugins.kindProjector,

  "ch.qos.logback"         % "logback-classic"             % "1.2.3",
  "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0",
  "org.scala-lang.modules" %% "scala-xml"                  % "1.2.0",
  "org.tpolecat"           %% "doobie-core"                % "0.8.8",
  "org.tpolecat"           %% "doobie-postgres"            % "0.8.8",
  "com.typesafe.play"      %% "play-json"                  % "2.8.1",
  "com.typesafe"           %  "config"                      % "1.4.0",
  "com.chuusai"            %% "shapeless"                  % "2.3.3",
  "io.lemonlabs"           %% "scala-uri"                  % "1.5.1",
  "io.monix"               %% "monix"                      % "3.0.0",
  "com.typesafe.akka"      %% "akka-actor-typed"           % "2.6.3",
  "com.softwaremill.quicklens" %% "quicklens"              % "1.4.12",

  "io.getquill"            %% "quill-jdbc"                 % "3.5.1",
  "org.flywaydb"            %  "flyway-core"                 % "6.4.2",

  Libraries.silencesAnnotation,
  Libraries.silencesCompilerPlugin,

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

  // actually should be moved to a different sub-project
  Libraries.scalaCheck,
  Libraries.scalaTest,
  Libraries.scalaTestPlus,
  Libraries.scalactic,

  // @newtype annotation
  Libraries.newtype,
  // refined types
  Libraries.refinedCore,

)

// SBT shell
//initialCommands in console := "import scalaz._, Scalaz._"
//initialCommands in console := "import cats.effect._, org.http4s._, org.http4s.dsl.io._, org.http4s.implicits._;" +
//  "implicit val timer : Timer[IO] = IO.timer(scala.concurrent.ExecutionContext.global)"
