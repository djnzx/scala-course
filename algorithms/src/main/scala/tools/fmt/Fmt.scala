package tools.fmt

object Fmt {
  def prettyMatrix[A](a: Array[Array[A]]): String =
    a.map(_.map(_.toString.length).max).max match {
      case w => a.map(_.map(x => s"%${w}d".format(x)).mkString(" ")).mkString("\n")
    }
  def printMatrix[A](a: Array[Array[A]]) = {
    println(prettyMatrix(a))
    println()
  }
}
