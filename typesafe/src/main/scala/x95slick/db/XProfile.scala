package slick.x95slick.db

import slick.jdbc.JdbcProfile

trait XProfile {
  /**
    * an abstract declaration,
    * should be declared explicitly
    * on implementation phase
    */
  val profile: JdbcProfile
}
