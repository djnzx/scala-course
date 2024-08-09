package lit

import org.typelevel.literally.Literally

case class Port(value: Int) extends AnyVal

object Port {
  /** code to validate */
  def fromString(raw: String): Either[String, Port] =
    Option(raw)
      .flatMap(_.toIntOption)
      .filter(_ > 0)
      .filter(_ <= 65535)
      .map(Port.apply)
      .toRight("invalid port - must be integer between 0 and 65535")

  object PortLiteral extends Literally[Port] {
    def validate(c: Context)(s: String): Either[String, c.Expr[Port]] = {
      import c.universe._
      Port.fromString(s)
        .map(_ => c.Expr(q"Port.fromString($s).get"))
    }

    def make(c: Context)(args: c.Expr[Any]*): c.Expr[Port] = this.apply(c)(args: _*)
  }

  implicit class port(val sc: StringContext) extends AnyVal {
    def port(args: Any*): Port = macro PortLiteral.make
  }

}
