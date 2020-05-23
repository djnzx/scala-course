package fps

object Fps062Bind extends App {
  def f(x: Int) = (x + 2, "a")
  def g(x: Int) = (x * 2, "b")
  def h(x: Int) = (x * x, "c")

  def bind(f: Int => (Int, String), p: (Int, String)): (Int, String) =
    f(p._1) match { case (i,s) => (i, p._2 + s) }

  val fr: (Int, String) = f(100)
  val gr: (Int, String) = bind(g, fr)
  val hr: (Int, String) = bind(h, gr)

  println(hr)

}
