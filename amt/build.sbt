val vCats = "2.6.1"
val vCatsEffects = "2.5.4"
val vFs2 = "2.5.10"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % vCats,
  "org.typelevel" %% "cats-effect" % vCatsEffects,
  "co.fs2" %% "fs2-core" % vFs2,
  "co.fs2" %% "fs2-reactive-streams" % vFs2,
  "org.typelevel" %% "munit-cats-effect-2" % "1.0.6",
  // wsdl impl
  "org.apache.axis" % "axis" % "1.4", // no transitive
  "org.apache.axis" % "axis-saaj" % "1.4", // no transitive
  "org.apache.axis" % "axis-jaxrpc" % "1.4", // no transitive
  "axis" % "axis-wsdl4j" % "1.5.1", // no transitive
  //      "wsdl4j" % "wsdl4j" % "1.6.3", // no transitive
  // just to avoid warnings in runtime
  "commons-discovery" % "commons-discovery" % "0.5", // transitive: commons-logging" % "commons-logging" % "1.1.1"
  "javax.mail" % "mail" % "1.4.7", // transitive: "javax.activation" % "activation" % "1.1",
)
