package slick.x95slick.db

import slick.jdbc.JdbcProfile
import scala.concurrent.duration.Duration

class DatabaseLayer(implicit val profile: JdbcProfile, implicit val timeout: Duration)
  extends XProfile        // here we have `val profile: JdbcProfile`
  with XTimeout          // here we have `val timeout: Duration`
  with Tables            // production Tables
  with TablesLearn       // learning ideas
  with DatabaseModule    // new db connection + exec method
  with DatabaseModuleOLD // old db connection + exec method
