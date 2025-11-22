package clipboard

import scala.sys.process.stringSeqToProcess
import scala.util.Try

object Clipboard {

  def get: Option[String] =
    Try(Seq("pbpaste").!!.stripTrailing()).toOption.filter(_.nonEmpty).filter(_.head.isDigit)

  def set(contents: String): Unit =
    Seq("pbcopy").#<(new java.io.ByteArrayInputStream(contents.getBytes("UTF-8"))).!

}
