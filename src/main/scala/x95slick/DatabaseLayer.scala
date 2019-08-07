package x95slick

import slick.jdbc.JdbcProfile

class DatabaseLayer(val profile: JdbcProfile)
  extends XProfile
  with DatabaseModule
  with DatabaseModuleOLD
