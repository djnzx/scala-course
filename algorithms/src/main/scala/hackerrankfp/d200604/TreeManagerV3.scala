package hackerrankfp.d200604

/** https://www.hackerrank.com/challenges/tree-manager/problem passes all the tests Immutable State but still uses
  * Mutable Tree
  */
object TreeManagerV3 {
  import scala.collection.mutable.ArrayBuffer
  import scala.util.Try
  // command representation
  sealed trait Command
  final case class CmdChangeValue(x: Int) extends Command // change current value
  final case object CmdPrint extends Command // print
  final case object CmdVisitLeft extends Command // navigate left
  final case object CmdVisitRight extends Command // navigate right
  final case object CmdVisitParent extends Command // navigate parent
  final case class CmdVisitChild(n: Int) extends Command // navigate to child N (from 1)
  final case class CmdInsertLeft(x: Int) extends Command // insert left rel to current
  final case class CmdInsertRight(x: Int) extends Command // insert right rel to current
  final case class CmdInsertChild(x: Int) extends Command // add a new child (to 0 pos)
  final case object CmdDelete extends Command
  // command parser
  def parse(s: String): Option[Command] = Try(s match {
    case s"change $x"       => CmdChangeValue(x.toInt)
    case "print"            => CmdPrint
    case "visit left"       => CmdVisitLeft
    case "visit right"      => CmdVisitRight
    case "visit parent"     => CmdVisitParent
    case s"visit child $n"  => CmdVisitChild(n.toInt)
    case s"insert left $x"  => CmdInsertLeft(x.toInt)
    case s"insert right $x" => CmdInsertRight(x.toInt)
    case s"insert child $x" => CmdInsertChild(x.toInt)
    case "delete"           => CmdDelete
    case _                  => ???
  }).toOption
  // tree representation
  // FIXME: mutable value and mutable children list
  final case class Node(var value: Int, children: ArrayBuffer[Node] = ArrayBuffer.empty)
  // path node representation
  final case class PathNode(link: Node, pos: Int, parent: Option[Node])
  // path representation: List[PathNode]
  case class Path(chain: List[PathNode]) {
    private def curr = chain.head
    private def currNode = curr.link
    private def curSiblings = curr.parent.get.children

    def value: Int = currNode.value
    def moveL = chain match {
      case h :: t => Path(h.copy(pos = h.pos - 1, link = curSiblings(h.pos - 2)) :: t)
      case _      => ???
    }
    def moveR = chain match {
      case h :: t => Path(h.copy(pos = h.pos + 1, link = curSiblings(h.pos)) :: t)
      case _      => ???
    }
    def moveUp = Path(chain.tail)
    def moveDown(n: Int) = Path(PathNode(currNode.children(n - 1), n, Some(currNode)) :: chain)
    // FIXME: tree mutation
    def changeValue(x: Int) = {
      currNode.value = x
      this
    }
    // FIXME: tree mutation
    def insertLeft(x: Int) = {
      curSiblings.insert(curr.pos - 1, Node(x))
      chain match {
        case h :: t => Path(h.copy(pos = h.pos + 1) :: t)
        case _      => ???
      }
    }
    // FIXME: tree mutation
    def insertRight(x: Int) = {
      curSiblings.insert(curr.pos, Node(x))
      this
    }
    // FIXME: tree mutation
    def insertChild(x: Int) = {
      currNode.children.prepend(Node(x))
      this
    }
    // FIXME: tree mutation
    def delete = {
      curSiblings.remove(curr.pos - 1)
      moveUp
    }
  }
  def run(path: Path, cmd: Command): (Path, Option[Int]) = cmd match {
    case CmdPrint => (path, Some(path.value))
    case _ =>
      (
        cmd match {
          case CmdChangeValue(x) => path.changeValue(x)
          case CmdVisitLeft      => path.moveL
          case CmdVisitRight     => path.moveR
          case CmdVisitParent    => path.moveUp
          case CmdVisitChild(n)  => path.moveDown(n)
          case CmdInsertLeft(x)  => path.insertLeft(x)
          case CmdInsertRight(x) => path.insertRight(x)
          case CmdInsertChild(x) => path.insertChild(x)
          case CmdDelete         => path.delete
          case _                 => ???
        },
        None,
      )
  }

  def process(data: List[String]) = {
    // initial state - list from one node points to our list with 1 elem
    val p0 = Path(List(PathNode(Node(0), 1, None)))
    val (_, outcome) = data
      .flatMap { parse }
      .foldLeft(
        (p0, List.empty[Int]),
      ) { case ((p, acc), cmd) =>
        val (p2, res) = run(p, cmd)
        res match {
          case Some(v) => (p2, v :: acc)
          case None    => (p2, acc)
        }
      }
    outcome.reverse.mkString("\n")
  }

  def body(line: => String): Unit = {
    val N = line.toInt
    val list = (1 to N).map { _ => line }.toList
    val r = process(list)
    println(r)
  }

  /** main to run from the console */
  //  def main(p: Array[String]): Unit = body { scala.io.StdIn.readLine }
  /** main to run from file */
  def main(p: Array[String]): Unit = processFile("treemanager100K.txt", body)
  def processFile(name: String, process: (=> String) => Unit): Unit = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
    scala
      .util
      .Using(
        scala.io.Source.fromFile(file),
      ) { src =>
        val it = src.getLines().map(_.trim)
        try { process(it.next()) }
        catch { case x: Throwable => x.printStackTrace() }
      }
      .fold(_ => ???, identity)
  }
}
