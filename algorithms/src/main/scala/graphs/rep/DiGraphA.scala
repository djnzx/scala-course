package graphs.rep

import tools.spec.ASpec

class DiGraphA(private val n: Int) extends DiGraph {
  private val vx = Array.fill[List[Int]](n)(List.empty)

  override def add(v: Int, w: Int): Unit = vx(v) = w :: vx(v)
  override def remove(v: Int, w: Int): Unit = vx(v) = vx(v).filter(_ != w)
  override def v: Int = vx.length
  override def adjTo(v: Int) = vx(v)
  override def vertices: Range = 0 until v
  override def toString: String = vertices
    .filter(vx(_).nonEmpty)
    .map { v =>
      vx(v).map { w => s"$v -> $w"}.mkString(", ")
    }.mkString("\n")
  override def reverse: DiGraph = {
    val g = new DiGraphA(n)
      vertices.foreach { v => 
        vx(v).foreach(w => g.add(w, v))
      }
    g
  }
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

class DiGraphASpec extends ASpec {
  it("reverse") {
    val g = new DiGraphA(5)
    g.add(1,2)
    g.add(2,3)
    // https://stackoverflow.com/questions/37875725/string-includes-many-substrings-in-scalatest-matchers
    g        .toString should (include("1 -> 2") and include("2 -> 3"))
    g.reverse.toString should (include("2 -> 1") and include("3 -> 2"))
  }
}