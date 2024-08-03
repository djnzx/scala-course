package abc

class Expression1

case class HashPartitioning2(expressions: Seq[Expression1], numPartitions: Int) extends T1 {

  def withP(np: Int) =
    copy(numPartitions = np)

}