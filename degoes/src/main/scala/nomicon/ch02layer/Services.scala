package nomicon.ch02layer

import zio.Has

object Services {

  type Logging = Has[Logging.Service]
  type UserRepo = Has[UserRepo.Service]

}
