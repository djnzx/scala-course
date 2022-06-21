package doobiex

object Db {

  private val env = sys.env

  val driver = "org.postgresql.Driver"
  val url = env.getOrElse("JDBC_URL", "jdbc:postgresql://localhost:5432/se2")
  val user = env.getOrElse("JDBC_USER", "postgres")
  val pass = env.getOrElse("JDBC_PASSWORD", "pg123456")
}
