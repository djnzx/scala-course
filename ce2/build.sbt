val vCats = "2.6.1"
val vCatsEffects = "2.5.4"
val vFs2 = "2.5.10"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % vCats,
  "org.typelevel" %% "cats-effect" % vCatsEffects,
  "org.typelevel" %% "cats-effect-laws" % vCatsEffects,
  "co.fs2" %% "fs2-core" % vFs2,
  "co.fs2" %% "fs2-reactive-streams" % vFs2,
  "org.typelevel" %% "munit-cats-effect-2" % "1.0.6",
)
