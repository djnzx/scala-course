package fp_red.red15

object Transducers2 extends App {
  val sourceData = Stream("Hello ", "my ", "dear. Hope ", "to see ", "you.", "Again. Again.")
  val expected = Stream("Hello  my  dear.", "Hope to see you.", "Again.", "Again.")
  type Transducer[I, O, S] = (Option[I], S) => (Option[O], S)
  
  import SimpleStreamTransducers._
  import SimpleStreamTransducers.Process._
  
  case class Sentence(value: String)
  
  def decomposeLine(src: String, acc: List[Sentence] = Nil): List[Sentence] = ???

  /**
    * (Some(line), buf) => (None,                          buf + line) - don't have delimiter, just collect to the buffer
    * (Some(line), buf) => (Some(List(Sentence)),          buf2 + ...) - one sentence extracted
    * (Some(line), buf) => (Some(List(sent1, sent2, ...)), buf2 + ...) - more sentences extracted
    * (None,       buf) => (Some(List(Sentence+.)),        Nil       ) - extract whatever we have from buf and build sentence          
    */
  def restoreSentences(line: Option[String], buf: List[String]): (Option[List[Sentence]], List[String]) = ???
  
  def processLine(line: String, buf: List[String] = Nil, acc: List[Sentence] = Nil): (List[Sentence], List[String]) =
    line match {
      case "" => (acc, buf)
      case _ =>
        line.indexOf('.') match {
        case -1  => (acc, buf :+ line)
        case pos =>
          val part1 = line.substring(0, pos + 1)
          val part2 = line.substring(pos + 1)
          val sentence = Sentence((buf :+ part1).mkString)
          processLine(part2, Nil, acc :+ sentence)    
      }
    }

  pprint.pprintln(processLine("a b c"))                 // (List(), List("a b c"))
  pprint.pprintln(processLine("a b c", List("d", "e"))) // (List(), List("d", "e", "a b c"))
  pprint.pprintln(processLine("a.b.c."))                // (List(Sentence(value = "a."), Sentence(value = "b."), Sentence(value = "c.")), List())
  pprint.pprintln(processLine("a.b.c"))                 // (List(Sentence(value = "a."), Sentence(value = "b.")), List("c"))
  pprint.pprintln(processLine("a.b.c", List("was ")))   // (List(Sentence(value = "a."), Sentence(value = "b.")), List("c"))
  
    
  
  
  
  
}

