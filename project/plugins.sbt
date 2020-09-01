// artima scalatest section
resolvers ++= Seq(
//  Repos.artima
  MavenRepository("Artima", "https://repo.artima.com/releases")
)

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

// scalatest section
//addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.10")

// compile Scala 3
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.4.1")
