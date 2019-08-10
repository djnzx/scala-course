package x95slick.impl

import x95slick.db.DatabaseLayer

final class SlickExperiments01 {
  private val persistence = new DatabaseLayer

  // import variables to local scope
  import persistence._
  import persistence.profile.api._ // actually we import slick.jdbc.PostgresProfile.api._

  def messages_schema_create: Unit = {
    println(messages.schema.createStatements.mkString)
    exec(messages.schema.create)
  }

  def messages_create: Unit = {
    val action = messages += Message(2,32,"Hello",Some(Flags.important))
    exec(action)
  }

//  def messages_read: Seq[Message] = {
  def messages_read: Unit = {
    val action = messages.filter(_.id > 0L)
    exec(action.result) foreach println
  }

}
