import Dependencies.Libraries.CompilerPlugins

libraryDependencies ++= Seq(
//  CompilerPlugins.betterMonadicFor,
//  CompilerPlugins.contextApplied,
//  CompilerPlugins.kindProjector,
  // CATS
  "org.typelevel" %% "cats-core" % "2.6.1",
  "org.typelevel" %% "cats-effect" % "3.2.9",
  // FS2
  "co.fs2" %% "fs2-core" % "3.1.5", // 3.2.9
  "co.fs2" %% "fs2-reactive-streams" % "3.1.5",
  // testing
  "org.typelevel" %% "munit-cats-effect-3" % "1.0.6",
//  Libraries.fs2core,
//  Libraries.fs2reactive,
//  // HTTP
//  Libraries.http4sServer,
//  Libraries.http4sDsl,
//  Libraries.http4sClient,
//  Libraries.http4sCirce,
//  Libraries.http4sJwtAuth,
//  // Serialization
//  Libraries.circeCore,
//  Libraries.circeGeneric,
//  Libraries.circeGenericEx,
//  Libraries.circeParser,
//  Libraries.circeRefined,
//  // @newtype annotation
//  Libraries.newtype,
//  // refined types
//  Libraries.refinedCore,
//  // shapeless
//  Libraries.shapeless,
//  "com.github.fd4s" %% "fs2-kafka" % "1.7.0",
//  "com.google.cloud" % "google-cloud-logging" % "3.0.1",
)
