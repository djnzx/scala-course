package hackerrankfp.d200604_09

import scala.util.Try

/**
  * https://www.hackerrank.com/challenges/tree-manager/problem
  */
object TreeManager {
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
  sealed trait XNode
  final case object NodeEmpty extends XNode
  final case class Node(value: Int, parent: XNode, children: Vector[XNode]) extends XNode
  // trie implementation
  class Trie {
    private var root: XNode = NodeEmpty
    private var curr: XNode = root
    /**
      * run the command
      * internally change the state
      * and optionally return the value
      */
    def run(cmd: Command): Option[Int] = {
      println(s"trie.run: $cmd")
      (cmd, curr) match {
        case (CmdPrint, Node(value, _, _)) => Some(value)
        case (_, _) => cmd match {
          case CmdChangeValue(x) =>
          case CmdVisitLeft      =>
          case CmdVisitRight     =>
          case CmdVisitParent    =>
          case CmdVisitChild(n)  =>
          case CmdInsertLeft(x)  =>
          case CmdInsertRight(x) =>
          case CmdInsertChild(x) =>
          case CmdDelete =>
          // TODO: remove after implementation
          case CmdPrint =>
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
    process(list)
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
      process(it.next())
    }.fold(_ => ???, identity)
  }
}
