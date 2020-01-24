name := "learn-scala-cook-book-aa"
version := "0.2.6"
scalaVersion := "2.13.1"

// https://alvinalexander.com/scala/sbt-how-specify-main-method-class-to-run-in-project
// sbt compile
// sbt run
mainClass in (Compile, run) := Some("_degoes.fp_to_the_max.steps.StepG3")

//javacOptions ++= Seq(
//  "-source", "1.8",
//  "-target", "1.8"
//)
scalacOptions ++= Seq(
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:existentials",

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
//  Resolver.sonatypeRepo("releases"),
//  Resolver.sonatypeRepo("snapshots")
  //  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
//  "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
)

val ZIOVersion        = "1.0.0-RC17"
val CatsVersion       = "2.0.0"
val MonixVersion      = "3.0.0"

// https://www.scala-sbt.org/release/docs/Library-Dependencies.html
libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "com.typesafe.akka" %% "akka-actor" % "2.5.23",

  "com.typesafe.slick" %% "slick" % "3.3.2",          // core
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2", // connection pool
  "org.postgresql" % "postgresql" % "42.2.8",         // database driver

  "com.typesafe.play" %% "play-json" % "2.7.4",       // JSON converter
  "com.chuusai" %% "shapeless" % "2.3.3",

//  "com.h2database" % "h2" % "1.4.197",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe" % "config" % "1.4.0",

  "org.typelevel" %% "cats-core"    % CatsVersion,
  "org.typelevel" %% "cats-effect"  % CatsVersion,
  "org.scalatest" %% "scalatest"    % "3.0.8",
  "dev.zio"       %% "zio"          % ZIOVersion,
  "dev.zio"       %% "zio-streams"  % ZIOVersion,
  "io.lemonlabs"  %% "scala-uri"    % "1.5.1",

  "io.monix"      %% "monix"        % MonixVersion,
  "org.tpolecat"  %% "doobie-core"  % "0.8.8",
//  "org.tpolecat" %% "doobie-postgres" % "0.8.8",
)
