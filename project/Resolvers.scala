import sbt.*

object Resolvers {

  val all = Seq(
    Resolver.mavenLocal,
    Resolver.mavenCentral,
    "confluent" at "https://packages.confluent.io/maven/",
    Resolver.typesafeRepo("releases"),
//    Resolver.sonatypeRepo("releases"),
//    Resolver.sonatypeRepo("snapshots"),
    Repos.artima,
  )

}
