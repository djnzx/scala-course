package nomicon.ch02layer

object Domain {
  
  case class UserId(id: Int) extends AnyVal
  case class User(id: UserId, name: String)

}
