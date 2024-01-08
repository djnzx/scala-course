package graphs.dfs

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

trait Graph {
  def add(src: Int, dst: Int): Unit
  def vertices: Set[Int]
  def children(vertex: Int): Set[Int]
}

class DiGraph extends Graph {

  import scala.collection.mutable
  private val edges: mutable.Map[Int, mutable.Set[Int]] = new mutable.HashMap
  private val noChildren                                = mutable.Set.empty[Int]

  override def add(src: Int, dst: Int): Unit =
    edges.get(src) match {
      case Some(children) => children.add(dst)
      case None           => edges.put(src, mutable.Set(dst))
    }

  override def vertices: Set[Int] =
    edges.values.flatten.toSet ++ edges.keys

  override def children(vertex: Int): Set[Int] =
    edges.getOrElse(vertex, noChildren).toSet

  override def toString: String =
    edges
      .map { case (v, ch) => v -> ch.toVector.sorted }
      .toList
      .sortBy { case (v, _) => v }
      .map { case (v, ch) => s"$v -> ${ch.mkString("[", ",", "]")}" }
      .mkString("\n")

}

object DiGraph {

  def apply(edges: (Int, Int)*): DiGraph = {
    val g = new DiGraph
    edges.foreach { case (src, dst) => g.add(src, dst) }
    g
  }

}

class DFS(g: Graph) {

  def traverse(src: Int, dst: Int): Set[Seq[Int]] = {

    def go(cur: Int, vs: Set[Int], ps: Set[List[Int]]): Set[Seq[Int]] =
      if (vs.contains(cur)) Set.empty
      else if (dst == cur) ps.map(cur :: _).map(_.reverse)
      else g.children(cur).flatMap(v => go(v, vs + cur, ps.map(cur :: _)))

    go(src, Set.empty, Set(List.empty))
  }

}

class DfsImplSpec extends AnyFunSuite with Matchers {

  test("apply & toString") {
    val g = DiGraph(
      1 -> 2,
      1 -> 3,
      2 -> 4,
      3 -> 4
    )
    println(g)
  }

  test("dfs 1") {
    val g     = DiGraph(
      1 -> 4,
      1 -> 2,
      2 -> 1,
      2 -> 3,
      3 -> 4,
      1 -> 5,
      5 -> 6,
      6 -> 4,
      1 -> 7,
      7 -> 4
    )
    val dfs   = new DFS(g)
    val paths = dfs.traverse(1, 4)
    paths.foreach(x => pprint.pprintln(x))

    paths shouldEqual Set(
      List(1, 2, 3, 4),
      List(1, 4),
      List(1, 5, 6, 4),
      List(1, 7, 4)
    )
  }

  test("dfs 2") {
    val g     = DiGraph(
      1  -> 2,
      1  -> 6,
      1  -> 10,
      2  -> 3,
      2  -> 4,
      6  -> 7,
      6  -> 9,
      3  -> 11,
      4  -> 5,
      5  -> 11,
      7  -> 8,
      9  -> 5,
      8  -> 11,
      10 -> 11
    )
    val dfs   = new DFS(g)
    val paths = dfs.traverse(1, 11)
    paths.foreach(x => pprint.pprintln(x))

    paths shouldEqual Set(
      List(1, 2, 3, 11),
      List(1, 2, 4, 5, 11),
      List(1, 6, 7, 8, 11),
      List(1, 6, 9, 5, 11),
      List(1, 10, 11)
    )
  }

}
