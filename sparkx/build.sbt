//scalaVersion := "2.12.13"
scalaVersion := "2.11.12"

scalacOptions -= ScalacOpts.macroAnnotations

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core"      % "2.4.7",
  "org.apache.spark" %% "spark-sql"       % "2.4.7",
)
