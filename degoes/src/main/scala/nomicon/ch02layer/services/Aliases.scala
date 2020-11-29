package nomicon.ch02layer.services

import zio.Has

/**
  * just aliases to our services
  */
object Aliases {

  type Logging = Has[Logging.Service]
  type UserRepo = Has[UserRepo.Service]
  type Configuration = Has[Configuration.Service]
  type DbConnection = Has[DbConnection.Service]

}
