package security

import scala.util.Try
import scala.util.Using

trait Files {

  def ux(msg: String): IllegalStateException = new IllegalStateException(msg)
  def fail(msg: String): Nothing = throw ux(msg)

  def mkPath(name: String): String =
    Try(getClass.getClassLoader.getResource(name))
      .map(_.toURI.getPath)
      .fold(_ => fail(s"file $name should exist"), identity)

  def loadFromResources(name: String): String =
    Try(getClass.getClassLoader.getResource(name))
      .map(_.toURI)
      // .map { x => pprint.log(x); x }
      .flatMap(jf => Using(scala.io.Source.fromFile(jf))(_.getLines().mkString("\n")))
      .fold(_ => fail(s"file $name should exist"), identity)

}
