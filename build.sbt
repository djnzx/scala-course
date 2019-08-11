name := "learn-scala-cook-book-aa"

scalacOptions ++= Seq(
  "-language:postfixOps"
//  , "-language:unchecked"
)
version := "0.2.4"
scalaVersion := "2.13.0"

resolvers ++= Seq(
//  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
//  "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
)

// https://www.scala-sbt.org/release/docs/Library-Dependencies.html
libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "com.typesafe.akka" %% "akka-actor" % "2.5.23",

  "com.typesafe.slick" %% "slick" % "3.3.2",          // core
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2", // connection pool
  "org.postgresql" % "postgresql" % "42.2.6",         // database driver

//  "com.h2database" % "h2" % "1.4.197",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
