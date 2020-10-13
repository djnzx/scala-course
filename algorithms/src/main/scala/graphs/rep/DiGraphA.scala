package graphs.rep

class DiGraphA(private val n: Int) extends DiGraph {
  private val vx = Array.fill[List[Int]](n)(List.empty)

  override def add(v: Int, w: Int): Unit = vx(v) = w :: vx(v)
//  override def remove(v: Int, w: Int): Unit = vx(v) = vx(v) - w
  override def v: Int = vx.length
  override def adjTo(v: Int) = vx(v)
  override def vertices: Range = 0 until v
  override def toString: String = vertices
    .filter(vx(_).nonEmpty)
    .map { v =>
      vx(v).map { w => s"$v -> $w"}.mkString(", ")
    }.mkString("\n")
}

object DiGraphA {
  /**
    * from Seq[(Int, Int)]
    */
  def from(n: Int, vs: Seq[(Int, Int)]): DiGraphA = {
    val g = new DiGraphA(n)
    vs.foreach { case (v, w) => g.add(v, w) }
    g
  }
  
  /** {{{
    * from Array[List[Int]]
    * }}}
    */
  def from(a: Array[List[Int]]): DiGraphA = {
    val g = new DiGraphA(a.length)
    a.indices.foreach { v =>
      a(v).foreach { w =>
        g.add(v, w)
      }
    }
    g
  }
  
}