package hackerrankfp.d200604_09

/**
  * https://www.hackerrank.com/challenges/tree-manager/problem
  * 5 out of 19 don't pass the time limits
  * https://en.wikipedia.org/wiki/Zipper_(data_structure)
  */
object TreeManager {
  import scala.collection.mutable.ArrayBuffer
  import scala.util.Try
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
  final case class Node(var value: Int, 
                        parent: Option[Node], 
                        var pos: Int = 1, 
                        children: ArrayBuffer[Node] = ArrayBuffer.empty) {
    def renumberChi = {
      children.zip(LazyList.from(1)).foreach { case (ch, n) => ch.pos = n }
      this
    }
  }
  // trie implementation
  class Trie {
    private val root: Node = Node(0, None)
    private var curr: Node = root
    /**
      * run the command
      * internally change the state
      * and optionally return the value
      */
    def run(cmd: Command): Option[Int] = {
      println(s"trie.run: $cmd")
      cmd match {
        case CmdPrint => Some(curr.value)
        case _ => cmd match {
          case CmdChangeValue(x) => curr.value = x
          case CmdVisitLeft => curr = curr.parent.get.children(curr.pos-2)
          case CmdVisitRight => 
            curr = curr.parent.get.children(curr.pos)
          case CmdVisitParent => curr = curr.parent.get
          case CmdVisitChild(n) => curr = curr.children(n-1)
          case CmdInsertLeft(x) =>
            val myParent = curr.parent.get
            val myPos = curr.pos - 1 // convert to 0-based
            val ch = myParent.children
            ch.insert(myPos, Node(x, Some(myParent), curr.pos))
            myParent.renumberChi
          case CmdInsertRight(x) =>
            val myParent = curr.parent.get
            val myPos0 = curr.pos - 1   // convert to 0-based
            val chi = myParent.children // all my siblings and me
            val node = Node(x, Some(myParent), curr.pos + 1)
            chi.insert(myPos0+1, node)
            myParent.renumberChi
          case CmdInsertChild(x) => 
            curr.children.prepend(Node(x, Some(curr), 0))
            curr.renumberChi
          case CmdDelete =>
            val newParent = curr.parent.get
            newParent.children.remove(curr.pos-1)
            newParent.renumberChi
            curr = newParent
        }
          None
      }
    }
  }

  def process(data: List[String]) = {
    val t = new Trie
    data flatMap parse flatMap t.run map { _.toString } mkString "\n"
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
  def main(p: Array[String]): Unit = processFile("treemanager.txt", body)
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
}
