package nomicon.ch02layer.domain

object Domain {

  case class UserId(id: Int) extends AnyVal
  case class User(id: UserId, name: String)

}
