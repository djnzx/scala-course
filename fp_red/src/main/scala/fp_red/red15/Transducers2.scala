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
    * (Some(line), buf) => (Nil,                     buf + line) - don't have delimiter, just collect to the buffer
    * (Some(line), buf) => (List(Sentence),          buf2 + ...) - one sentence extracted
    * (Some(line), buf) => (List(sent1, sent2, ...), buf2 + ...) - more sentences extracted
    * (None,       buf) => (List(Sentence+.),        Nil       ) - extract whatever we have from buf and build sentence          
    */
  def restoreSentences(line: Option[String], buf: List[String]): (List[Sentence], List[String]) = ???
  
  
  
  
  
  
}

