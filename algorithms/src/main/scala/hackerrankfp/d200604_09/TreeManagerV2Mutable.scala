package hackerrankfp.d200604_09

/** https://www.hackerrank.com/challenges/tree-manager/problem passes all the tests, but still uses Mutable Tree and
  * mutable Path
  */
object TreeManagerV2Mutable {
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
  final case class Node(var value: Int, children: ArrayBuffer[Node] = ArrayBuffer.empty)
  // path node representation
  final case class PathNode(link: Node, var pos: Int, parent: Option[Node]) {
    def this(link: Node, pos: Int, parent: Node) = this(link, pos, Some(parent))
  }
  object PathNode {
    def root(node: Node) = PathNode(node, 1, None)
  }

  // path representation
  class Path(root: Node) {
    var chain = List(PathNode.root(root))
    private def curr: PathNode = chain.head
    private def parent: Node = curr.parent.get
    private def currNode: Node = curr.link
    private def currPos: Int = curr.pos
    private def curChi: ArrayBuffer[Node] = currNode.children
    private def curSiblings: ArrayBuffer[Node] = parent.children

    def currVal: Int = currNode.value
    def changeValue(x: Int): Unit = currNode.value = x
    def moveL: Unit = chain match {
      case h :: t => chain = h.copy(pos = h.pos - 1, link = parent.children(h.pos - 2)) :: t
      case _      => ???
    }
    def moveR: Unit = chain match {
      case h :: t => chain = h.copy(pos = h.pos + 1, link = parent.children(h.pos)) :: t
      case _      => ???
    }
    def moveUp: Unit = chain = chain.tail
    def moveDown(n: Int): Unit = chain = new PathNode(currNode.children(n - 1), n, currNode) :: chain
    def insertLeft(x: Int): Unit = {
      curSiblings.insert(currPos - 1, Node(x))
      curr.pos += 1
    }
    def insertRight(x: Int): Unit =
      curSiblings.insert(currPos, Node(x))
    def insertChild(x: Int): Unit =
      curChi.prepend(Node(x))
    def delete: Unit = {
      curSiblings.remove(currPos - 1)
      moveUp
    }
  }
  // trie implementation
  class Trie {
    private val path: Path = new Path(Node(0))
    def run(cmd: Command): Option[Int] = {
      cmd match {
        case CmdPrint => Some(path.currVal)
        case _ =>
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
          }
          None
      }
    }
  }

  // ////////////////////////////////////////////////////////////////////
  def process(data: List[String]) = {
    val t = new Trie
    val r = data flatMap parse flatMap t.run map { _.toString } mkString "\n"
    r
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
