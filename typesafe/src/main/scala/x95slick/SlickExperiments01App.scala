package x95slick

import slick.x95slick.impl.SlickExperiments01

object SlickExperiments01App extends App {
  val exp = new SlickExperiments01
  import exp._

//  messages_schema_create
//  messages_create
//  messages_read foreach println
  messages_read_all
  messages_read_some1
  messages_read_some2
  messages_read_none
}
