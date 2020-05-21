package hr_parse.db

object MigrateApp extends App with DbSetup {
  dbSetup(
    true
  )
}
