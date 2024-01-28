package classnaming

object ScalaClassName {

  private def isDigit(xs: String): Boolean = xs.forall(_.isDigit)

  private val delimiters = Array('$', '.')

  def makeClassName(x: Any): String = {
    val chunks = x.getClass.getName.split(delimiters)

    if (isDigit(chunks.last))
      chunks(chunks.length - 2)
    else
      chunks.last
  }

}
