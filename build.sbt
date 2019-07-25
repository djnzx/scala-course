name := "learn-scala-cook-book-aa"

version := "0.2"
scalaVersion := "2.12.8"

resolvers ++= Seq(
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases/",
//  "Java.net Maven2 Repository" at "http://download.java.net/maven/2/"
)

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  "com.typesafe.akka" %% "akka-actor" % "2.5.23"
)
