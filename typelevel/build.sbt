import Dependencies.Libraries
import Dependencies.Libraries.CompilerPlugins

libraryDependencies ++= Seq(
  CompilerPlugins.betterMonadicFor,
  CompilerPlugins.contextApplied,
  CompilerPlugins.kindProjector,
  // CATS
  Libraries.cats,
  Libraries.catsLaws,
  Libraries.catsEffect,
  Libraries.catsMtlCore,
  "dev.profunktor" %% "console4cats" % "0.8.1",
  // FS2
  Libraries.fs2core,
  Libraries.fs2reactive,
  // HTTP
  Libraries.http4sServer,
  Libraries.http4sDsl,
  Libraries.http4sClient,
  Libraries.http4sCirce,
  Libraries.http4sJwtAuth,
  // Serialization
  Libraries.circeCore,
  Libraries.circeGeneric,
  Libraries.circeGenericEx,
  Libraries.circeParser,
  Libraries.circeRefined,
  // @newtype annotation
  Libraries.newtype,
  // refined types
  Libraries.refinedCore,
  // shapeless
  Libraries.shapeless,
  "com.github.fd4s" %% "fs2-kafka" % "1.7.0",
  "com.google.cloud" % "google-cloud-logging" % "3.0.1",
)
