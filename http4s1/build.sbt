lazy val http4sVersion = "1.0.0-M25"

def http4s(artifact: String) = "org.http4s" %% artifact % http4sVersion

libraryDependencies ++= Seq(
  http4s("http4s-core"),
  http4s("http4s-dsl"),
  http4s("http4s-blaze-server"),
  http4s("http4s-circe"),
)
