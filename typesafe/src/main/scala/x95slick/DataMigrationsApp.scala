package x95slick

import slick.x95slick.impl.MigrationSteps

object DataMigrationsApp extends App {
  val mig = new MigrationSteps
  import mig._

  move_countries
  move_vendors
  create_partnumbers
}
