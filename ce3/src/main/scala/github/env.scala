package github

object env {

  def token: Option[String] = Option(System.getenv("GITHUB_TOKEN"))

}
