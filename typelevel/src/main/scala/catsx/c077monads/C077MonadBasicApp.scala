package catsx.c077monads

object C077MonadBasicApp extends App {
  def parse(s: String): Option[Int] = try {
    Some(s.toInt)
  } catch {
    case _: NumberFormatException => None
  }

  def divide(a: Int, b: Int): Option[Int] =
    if (b == 0) None else Some(a / b)

  def calc(as: String, bs: String): Option[Int] =
    for {
      a <- parse(as)
      b <- parse(bs)
      r <- divide(a, b)
    } yield r

  def calc2(as: String, bs: String): Option[Int] =
    parse(as).flatMap(a =>
      parse(bs).flatMap(b =>
        divide(a, b)
      )
    )

}
