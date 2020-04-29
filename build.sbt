import Dependencies.Libraries

name         := "learn-scala-ar"
version      := "20.4.28"
scalaVersion := "2.13.1"

// https://alvinalexander.com/scala/sbt-how-specify-main-method-class-to-run-in-project
// sbt compile
// sbt run
mainClass in (Compile, run) := Some("degoes.fp_to_the_max.steps.StepG3")

javacOptions ++= Seq(
//  "-source", "1.8",
//  "-target", "1.8"
)
scalacOptions ++= Seq(
  "-deprecation",
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:existentials",

//  "-Ypartial-unification", // by default since 2.13
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Yrepl-class-based",
  "-Ywarn-extra-implicit",
  "-Ywarn-unused:_,imports",
  "-Ywarn-unused:imports",
  "-Yrangepos",

  "-deprecation",
  "-encoding", "UTF-8",
  "-explaintypes",
  "-feature",
  "-unchecked",
  // https://docs.scala-lang.org/overviews/compiler-options/index.html#Warning_Settings
  // http://eed3si9n.com/stricter-scala-with-xlint-xfatal-warnings-and-scalafix
  "-Xlint:_,-type-parameter-shadow",
  "-opt-warnings",
  "-opt:l:inline",
  "-opt-inline-from:<source>",
  //  "-Xfatal-warnings",     // treat warning as fatal. 53 warnings @ Nov 30
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

lazy val akkaVersion       = "2.6.3"
lazy val ZIOVersion        = "1.0.0-RC18-2"
lazy val CatsVersion       = "2.0.0"
lazy val CatsEffectVersion = "2.1.1"
lazy val MonixVersion      = "3.0.0"
lazy val ScalaZVersion     = "7.2.30"
lazy val playVersion       = "2.8.1"
lazy val slickVersion      = "3.3.2"
lazy val pgDriverVersion   = "42.2.10"

// https://www.scala-sbt.org/release/docs/Library-Dependencies.html
libraryDependencies ++= Seq(
  "ch.qos.logback"         % "logback-classic"             % "1.2.3",
  "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0",
  "org.scala-lang.modules" %% "scala-xml"                  % "1.2.0",
  "org.postgresql"         % "postgresql"                  % pgDriverVersion,
  //  "com.h2database" % "h2" % "1.4.200",
  "org.tpolecat"           %% "doobie-core"                % "0.8.8",
  "org.tpolecat"           %% "doobie-postgres"            % "0.8.8",
  "com.typesafe.slick"     %% "slick"                      % slickVersion,
  "com.typesafe.slick"     %% "slick-hikaricp"             % slickVersion,
  "com.typesafe.play"      %% "play-json"                  % playVersion,
  "com.typesafe"           %  "config"                      % "1.4.0",
  "com.chuusai"            %% "shapeless"                  % "2.3.3",
  "io.lemonlabs"           %% "scala-uri"                  % "1.5.1",
  "io.monix"               %% "monix"                      % MonixVersion,
  "org.typelevel"          %% "cats-core"                  % CatsVersion,
  "org.typelevel"          %% "cats-effect"                % CatsEffectVersion,
  "dev.zio"                %% "zio"                        % ZIOVersion,
  "dev.zio"                %% "zio-streams"                % ZIOVersion,
  "org.scalaz"             %% "scalaz-core"                % ScalaZVersion,
  "org.scalaz"             %% "scalaz-effect"              % ScalaZVersion,
  "com.typesafe.akka"      %% "akka-actor-typed"           % akkaVersion,
//  "com.typesafe.akka"      %% "akka-actor-testkit-typed"   % akkaVersion     % Test,

  "com.softwaremill.quicklens" %% "quicklens"              % "1.4.12",

  // Serialization
  Libraries.circeCore,
  Libraries.circeGeneric,
  Libraries.circeParser,
  Libraries.circeRefined,

  // HTTP
  Libraries.http4sDsl,
  Libraries.http4sServer,
  Libraries.http4sClient,
  
  Libraries.http4sCirce,
  Libraries.http4sJwtAuth,

  // actually should be moved to a different sub-project
  Libraries.scalaCheck,
  Libraries.scalaTest,
  Libraries.scalaTestPlus,
  Libraries.scalactic,
)

// SBT shell
initialCommands in console := "import scalaz._, Scalaz._"
