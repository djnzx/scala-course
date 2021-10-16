package d00

/** https://failex.blogspot.com/2017/04/the-high-cost-of-anyval-subclasses.html
  */
object Failed extends App {

  class Label(val s: String) extends AnyVal

  object Label {
    def apply(s: String): Label = new Label(s)
  }

}
