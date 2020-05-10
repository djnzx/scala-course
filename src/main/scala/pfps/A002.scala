package pfps

import io.estatico.newtype.macros.newtype

object A002 {

  /**
   * https://github.com/estatico/scala-newtype
   * we need the macro paradise compiler plugin in Scala versions below 2.13.0
   * extra compiler flag -Ymacro-annotations in versions 2.13.0+
   * Newtypes will eventually be replaced by opaque types
   *
   * - eliminating the extra allocation issue
   * - removing the copy method
   */
  @newtype case class Username(value: String)
  @newtype case class Email(value: String)

  val em: Email = Email("aaa")
//  "foo".coerce[Email]




}
