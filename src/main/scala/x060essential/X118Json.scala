package x060essential

/**
  * algebraic datatype
  * abstract syntax tree
  */
object X118Json extends App {
  sealed trait Json {
    def print: String = {
      def quote(src: String): String = s""""$src""""
      def seqToJson(cell: JSeqCell): String = cell match {
        case JSeqCell(h, t @ JSeqCell(_,_)) => s"${h.print}, ${seqToJson(t)}"
        case JSeqCell(h, JSeqEnd) => h.print
      }
      def objToJson(obj: JObjCell): String = obj match {
        case JObjCell(k, v, t @ JObjCell(_,_,_)) => s"${quote(k)}: ${v.print}, ${objToJson(t)}"
        case JObjCell(k, v, JObjEnd) => s"${quote(k)}: ${v.print}"
      }
      this match {
        case JNull => "null"
        case JNumber(n) => n.toString
        case JBoolean(b) => b.toString
        case JString(s) => quote(s)
        case s:JSeqCell => s"[${seqToJson(s)}]"
        case JSeqEnd => "[]"
        case o:JObjCell => s"{${objToJson(o)}}"
        case JObjEnd => "{}"
      }
    }
  }
  final case object JNull extends Json
  final case class JNumber(n: Double) extends Json
  final case class JString(s: String) extends Json
  final case class JBoolean(b: Boolean) extends Json

  sealed trait JSeq extends Json
  final case class JSeqCell(head: Json, tail: JSeq) extends JSeq
  final case object JSeqEnd extends JSeq

  sealed trait JObj extends Json
  final case class JObjCell(key: String, value: Json, tail: JObj) extends JObj
  final case object JObjEnd extends JObj

  val j1: Json =
    JSeqCell(JString("a string"),
      JSeqCell(JNumber(1.0),
        JSeqCell(JBoolean (true), JSeqEnd)))

  val j21 =
    JSeqCell(JNumber(1.0),
      JSeqCell(JNumber(2),
        JSeqCell(JNumber(3.0), JSeqEnd)))

  val j22 =
    JSeqCell(JString("a"),
      JSeqCell(JString("b"),
        JSeqCell(JString("c"), JSeqEnd)))

  val j23 =
    JObjCell("z", JBoolean(true),
      JObjCell("x", JBoolean(false),
        JObjCell("v", JNumber(1.0), JObjEnd)))

  val j2: Json =
    JObjCell("a", j21,
      JObjCell("b", j22,
        JObjCell("c", j23, JObjEnd)))

  println(j1.print)
  println(j21.print)
  println(j22.print)
  println(j23.print)
  println(j2.print)
}
