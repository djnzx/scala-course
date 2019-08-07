package x95slick

object MainApp extends App {

  val persistence = new DatabaseLayer(slick.jdbc.PostgresProfile)

//  persistence.exec()
}
