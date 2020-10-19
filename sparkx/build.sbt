scalaVersion := "2.12.12"
scalacOptions -= ScalacOpts.macroAnnotations

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core"      % "3.0.1",
  "org.apache.spark" %% "spark-sql"       % "3.0.1",
  //      "org.apache.spark" %% "spark-streaming" % "3.0.1",
)
