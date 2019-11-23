package x060essential

object X240ContextBounds extends App {
  trait Htmlable {
    def toHtml: String
  }

  trait HtmlWriter[A] {
  }

  def pageTemplate1[A](body: A)(implicit writer: HtmlWriter[A]): String = ???
  // this syntax means type A must have `HtmlWriter` implemented
  def pageTemplate2[A : HtmlWriter](body: A): String = ???
  // [A : Context] - expands to [A](implicit ev: Context[A])
  // dont use implicit conversions !!!
  // use type enrichment via implicit classes
}
