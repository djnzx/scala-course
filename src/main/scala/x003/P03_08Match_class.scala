package x003

object P03_08Match_class extends App {
  trait Command {
    def act(s: String) = println(s)
  }
  case object Start extends Command
  case object Stop extends Command
  case object Go extends Command
  case object Act extends Command

  def executeCmd(c: Command) = c match {
    case Start | Go => c.act("start")
    case Stop => c.act("stop")
    case Act => c.act("act")
    case _ => println("unknown command")
  }

  executeCmd(Start)
  executeCmd(Stop)
}
