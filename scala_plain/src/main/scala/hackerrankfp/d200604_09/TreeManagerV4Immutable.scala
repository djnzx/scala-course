package hackerrankfp.d200604_09

/**
  * https://www.hackerrank.com/challenges/tree-manager/problem
  * passes all the tests,
  * Immutable State, Immutable Tree
  */
object TreeManagerV4Immutable {
  import scala.util.Try
  import scala.collection.immutable.VectorBuilder
  // command representation 
  sealed trait Command
  final case class CmdChangeValue(x: Int) extends Command // change current value
  final case object CmdPrint extends Command              // print
  final case object CmdVisitLeft extends Command          // navigate left
  final case object CmdVisitRight extends Command         // navigate right
  final case object CmdVisitParent extends Command        // navigate parent
  final case class CmdVisitChild(n: Int) extends Command  // navigate to child N (from 1)
  final case class CmdInsertLeft(x: Int) extends Command  // insert left rel to current
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
  }).toOption
  // tree representation
  final case class Node(value: Int, children: Vector[Node] = Vector.empty)
  // path node representation
  final case class PathNode(link: Node, pos: Int, parent: Option[Node])
  // recursively update two immutable structures
  def updated(chain: List[PathNode], newNode: Node): List[PathNode] = chain match {
    case h::Nil =>
      val newPathNode = h.copy(link = newNode)
      newPathNode::Nil
    case h::t =>
      val parent = h.parent.get
      val newChildren = parent.children.updated(h.pos-1, newNode)
      val newParent = h.parent.get.copy(children = newChildren)
      val newPathNode = h.copy(link = newNode, parent = Some(newParent))
      newPathNode::updated(t, newParent)
  }
  // path representation: List[PathNode]
  case class Path(chain: List[PathNode]) {
    private def curr        = chain.head
    private def currNode    = curr.link
    private def curSiblings = curr.parent.get.children
    private def insertAt[A](v: Vector[A], pos: Int, x: A): Vector[A] = {
      val (left, right) = (v.slice(0, pos), v.slice(pos, v.size))
      val vb = new VectorBuilder[A]
      vb ++= left
      vb += x
      vb ++= right
      vb.result()
    } 
    private def removeAt[A](v: Vector[A], pos: Int): Vector[A] = {
      val left = v.slice(0, pos)
      val right = v.slice(pos+1, v.size)
      left ++ right
    } 
    
    def value: Int = currNode.value
    def moveL = chain match { case h::t => Path(h.copy(pos = h.pos - 1, link = curSiblings(h.pos-2))::t) }
    def moveR = chain match { case h::t => Path(h.copy(pos = h.pos + 1, link = curSiblings(h.pos))::t) }
    def moveUp = Path(chain.tail)
    def moveDown(n: Int) = Path(PathNode(currNode.children(n-1), n, Some(currNode))::chain)
    def changeValue(x: Int) = {
      val newNode = currNode.copy(value = x)
      Path(updated(chain, newNode))
    }
    def insertLeft(x: Int) = {
      val newParent = curr.parent.get.copy(children = insertAt(curSiblings, curr.pos-1, Node(x)))
      Path(chain match {
        case h::t => h.copy(parent = Some(newParent), pos = h.pos + 1)::updated(t, newParent)
      })
    }
    def insertRight(x: Int) = {
      val newParent = curr.parent.get.copy(children = insertAt(curSiblings, curr.pos, Node(x)))
      Path(chain match {
        case h::t => h.copy(parent = Some(newParent))::updated(t, newParent)
      })
    }
    def insertChild(x: Int) = {
      val newChildren = Node(x) +: currNode.children
      val newNode = currNode.copy(children = newChildren)
      Path(updated(chain, newNode))
    }
    def delete = {
      val newParentChildren = removeAt(curSiblings, curr.pos-1)
      val newParent = curr.parent.get.copy(children = newParentChildren)
      Path(chain match {
        case _::t => updated(t, newParent)
      })
    }
  }
  // run one command on the state
  def run(path: Path, cmd: Command): (Path, Option[Int]) = cmd match {
    case CmdPrint => (path, Some(path.value))
    case _ => (cmd match {
      case CmdChangeValue(x) => path.changeValue(x)
      case CmdVisitLeft      => path.moveL
      case CmdVisitRight     => path.moveR
      case CmdVisitParent    => path.moveUp
      case CmdVisitChild(n)  => path.moveDown(n)
      case CmdInsertLeft(x)  => path.insertLeft(x)
      case CmdInsertRight(x) => path.insertRight(x)
      case CmdInsertChild(x) => path.insertChild(x)
      case CmdDelete         => path.delete
    }, None)
  }
  // process all commands
  def process(data: List[String]) = {
    // initial state - list from one node points to our list with 1 elem
    val p0 = Path(List(PathNode(Node(0), 1, None)))
    val (_, outcome) = data.flatMap { parse }.foldLeft(
      (p0, List.empty[Int])
    ) { case ((p, acc), cmd) =>
      val (p2, res) = run(p, cmd)
      res match {
        case Some(v) => (p2, v::acc)
        case None    => (p2,    acc)
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
  def main(p: Array[String]): Unit = processFile("treemanager2.txt", body)
  def processFile(name: String, process: (=> String) => Unit): Unit = {
    val file = new java.io.File(this.getClass.getClassLoader.getResource(name).getFile)
    scala.util.Using(
      scala.io.Source.fromFile(file)
    ) { src =>
      val it = src.getLines().map(_.trim)
      try { process(it.next()) }
      catch { case x: Throwable => x.printStackTrace() }
    }.fold(_ => ???, identity)
  }
  def now = System.currentTimeMillis()
  def delta(t0: Long): Long = System.currentTimeMillis() - t0
}
