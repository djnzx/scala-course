package slick.x95slick.impl

import slick.x95slick.db.DatabaseLayer

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
    val action = messages ++= Seq(
//      Message(2,32,"Hello",Some(Flags.normal)),
//      Message(5,31,"Hi there",None)
      Message(6,31,"Ciao",None)
    )
    exec(action)
  }

//  def messages_read: Seq[Message] = {
  def messages_read_all: Unit = {
    val action = messages.filter(_.id > 0L)
    exec(action.result) foreach println
  }

  def messages_read_some1: Unit = {
    val action1 = messages.filter(m => m.flag === (Important:MsgFlag))
    val action2 = messages.filter(m => m.flag === Flags.important)
    val action3 = messages.filter(_.flag === (Important:MsgFlag))
    val action4 = messages.filter(_.flag === Flags.important)
    val action5 = messages.filter(
      _.flag === (Flags.important)
    )
    println("Some (important)")
    exec(action3.result) foreach println
  }

  // Some: SELECT ... FROM ... WHERE column IS NOT NULL
  def messages_read_some2: Unit = {
    val action = messages.filter(_.flag.isDefined)
    println("Some (all)")
    exec(action.result) foreach println
  }

  // None: SELECT ... FROM ... WHERE column IS NULL
  def messages_read_none: Unit = {
    val action = messages.filter(_.flag.isEmpty)
    println("None")
    exec(action.result) foreach println
  }

}
