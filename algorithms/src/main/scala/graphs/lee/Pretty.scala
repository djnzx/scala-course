package graphs.lee

object Pretty {

  def colorize(content: String, color: String): String = color.concat(content).concat(Console.RESET)

}
