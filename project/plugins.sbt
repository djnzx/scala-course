resolvers += MavenRepository("Artima", "https://repo.artima.com/releases")
resolvers ++= Resolver.sonatypeOssRepos("public")

/** ScalaFmt Plugin: https://github.com/scalameta/sbt-scalafmt
  * Plugin by itself: https://github.com/scalameta/scalafmt Documentation:
  * https://scalameta.org/scalafmt/docs/installation.html
  */
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")

/** dependency tree, bundled into sbt since 1.4 */
addDependencyTreePlugin

/** Metals (https://scalacenter.github.io/bloop/docs/build-tools/sbt)
  * moved to the different file, to allow Metals plugin manage it automatically
  */
//addSbtPlugin("ch.epfl.scala" % "sbt-bloop" % "1.5.6")

/** NativePackager (https://github.com/sbt/sbt-native-packager) */
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.16")

/** https://github.com/spray/sbt-revolver */
//addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

/** scalatest section */
//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.10")

/** https://github.com/sbt/sbt-buildinfo */
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.11.0")

/** XML binding (https://github.com/eed3si9n/scalaxb, https://scalaxb.org/sbt-scalaxb) */
addSbtPlugin("org.scalaxb" % "sbt-scalaxb" % "1.11.0")

/** ProtoBuf (https://github.com/thesamet/sbt-protoc) */
addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.7")

/** ProtoBuf (https://github.com/scalapb/ScalaPB, https://scalapb.github.io) */
libraryDependencies += "com.thesamet.scalapb" %% "compilerplugin" % "0.11.15"

/** named options: (https://github.com/DavidGregory084/sbt-tpolecat) */
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.4.1")

/** no-publish: https://github.com/ChristopherDavenport/sbt-no-publish */
addSbtPlugin("io.chrisdavenport" % "sbt-no-publish" % "0.1.0")

/** exploring mdoc https://scalameta.org/mdoc/ */
addSbtPlugin("org.scalameta" % "sbt-mdoc" % "2.5.2")

/** explore laika https://typelevel.org/Laika */
addSbtPlugin("org.typelevel" % "laika-sbt" % "1.0.1")
