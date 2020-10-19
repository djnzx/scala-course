package tools.fmt

object Fmt {

  /**
    * generates the pad
    * by repeating the given char
    * to fill the difference between width and length
    */
  def pad(length: Int, width: Int, char: Char = ' ') = char.toString * ((width - length) max 0)

  def leftPad(x: Any, width: Int) = x.toString match { case xs => pad(xs.length, width) + xs }

  def rightPad(x: Any, width: Int) = x.toString match { case xs => xs + pad(xs.length, width) }

  def boolToString(b: Boolean) = if (b) "T" else "F"

  def colorBool(b: Boolean, width: Int = 2) = (b, leftPad(boolToString(b), width)) match {
    case (true, s) => s"${Console.GREEN}$s${Console.RESET}"
    case (false, s) => s
  }

  def printIndices(r: Range, width: Int = 2, prefix: String = "") = {
    print(prefix)
    val as = r.map(rightPad(_, width)).mkString
    println(s"${Console.BLUE}$as${Console.RESET}")
  }

  def printArray[A](a: Array[A], width: Int = 2, prefix: String = "") = {
    print(prefix)
    val as = a.map(x => leftPad(x, width)).mkString
    println(as)
  }
  
  def printLine(n: Int, ch: Char = '-') = println(ch.toString * n)
  
  /**
    * formats matrix according to the biggest value in it
    * @return
    */
  def prettyMatrix[A](a: Array[Array[A]]): String =
    a.map(_.map(_.toString.length).max).max match {
      case w => a
        .map(_.map(leftPad(_, w + 1)).mkString)
        .mkString("\n")
    }

  def printMatrix[A](a: Array[Array[A]], msg: String = "") = {
    if (msg.nonEmpty) println(msg)

    println(prettyMatrix(a))
    println()
  }

  def printBoolMatrix(a: Array[Array[Boolean]], width: Int = 2, msg: String = "", linePrefix: () => String = () => "") = {
    if (msg.nonEmpty) println(msg)
    
    a.map { ai =>
      linePrefix() + ai.map(b => colorBool(b, width)).mkString
    }.foreach(println)

    println
  }

}
