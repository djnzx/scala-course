package classnaming

object ScalaClassName {

  private def isDigit(x: String): Boolean = x.forall(c => c >= '0' && c <= '9')

  private val delimiters = Array('$', '.')

  def makeClassName(x: Any): String = {
    val chunks = x.getClass.getName.split(delimiters)

    if (isDigit(chunks.last))
      chunks(chunks.length - 2)
    else
      chunks.last
  }

}
