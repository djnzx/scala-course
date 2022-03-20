package course.d1

object A0Logic {

  def evalFileName(name: String): String =
    Option(getClass.getClassLoader.getResource(name))
      .map(_.getFile)
      .getOrElse(throw new IllegalArgumentException(s"File `$name` Not Found"))

  def fToC(t: Double): Double = (t - 32.0) * (5.0 / 9.0)
  def cToF(t: Double): Double = t * 9.0 / 5.0 + 32

}
