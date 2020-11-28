package nomicon.ch02layer.services

import zio.Has

/**
  * just aliases to our services
  */
object Aliases {

  type Logging = Has[Logging.Service]
  type UserRepo = Has[UserRepo.Service]

}
