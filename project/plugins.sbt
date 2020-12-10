// artima scalatest section
resolvers ++= Seq(
//  Repos.artima
  MavenRepository("Artima", "https://repo.artima.com/releases")
)

// https://github.com/spray/sbt-revolver
//addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

// scalatest section
//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.10")

// compile Scala 3
// https://github.com/scala/scala3-example-project/blob/master/project/plugins.sbt
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.4.6")

// https://github.com/sbt/sbt-buildinfo
//addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.10.0")
