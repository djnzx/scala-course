name := "learn-scala-cook-book-aa"

version := "0.2"

scalaVersion := "2.12.8"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.23",
)
