resolvers ++= Seq(
  MavenRepository("Artima", "https://repo.artima.com/releases"),
  Resolver.sonatypeRepo("public"),
)

/** ScalaFmt: https://github.com/scalameta/sbt-scalafmt */
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.4")

/** dependency tree, bundled into sbt since 1.4 */
addDependencyTreePlugin

/** Metals (https://scalacenter.github.io/bloop/docs/build-tools/sbt) */
addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.4.11")

// https://github.com/spray/sbt-revolver
//addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

// scalatest section
//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.10")

// https://github.com/sbt/sbt-buildinfo
//addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.10.0")

/** XML binding (https://github.com/eed3si9n/scalaxb, https://scalaxb.org/sbt-scalaxb) */
addSbtPlugin("org.scalaxb" % "sbt-scalaxb" % "1.8.2")

/** ProtoBuf (https://github.com/thesamet/sbt-protoc) */
addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.0")

/** ProtoBuf (https://github.com/scalapb/ScalaPB, https://scalapb.github.io) */
libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.6"
