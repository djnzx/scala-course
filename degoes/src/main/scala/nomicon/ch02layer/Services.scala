package nomicon.ch02layer

import nomicon.ch02layer.services.{Logging, UserRepo}
import zio.Has

object Services {

  type Logging = Has[Logging.Service]
  type UserRepo = Has[UserRepo.Service]

}
