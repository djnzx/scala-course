package tools.fmt

object Fmt {

  def prettyMatrix[A](a: Array[Array[A]]): String =
    a.map(_.map(_.toString.length).max).max match {
      case w => a.map(_.map(x => s"%${w}d".format(x)).mkString(" ")).mkString("\n")
    }

  def printMatrix[A](a: Array[Array[A]], msg: String = "") = {
    if (msg.nonEmpty) println(msg)

    println(prettyMatrix(a))
    println()
  }

  def printBoolMatrix(a: Array[Array[Boolean]], msg: String = "") = {
    if (msg.nonEmpty) println(msg)
    val as = a.map(_.map(if (_) "T" else "F").mkString(" "))

    println(as.mkString("\n"))
    println
  }

}
