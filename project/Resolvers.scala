import sbt.Resolver

object Resolvers {

  val all = Seq(
    Resolver.mavenLocal,
    Resolver.mavenCentral,
//    Resolver.typesafeRepo("releases"),
//    Resolver.sonatypeRepo("releases"),
//    Resolver.sonatypeRepo("snapshots"),
    Repos.artima,
  )

}
