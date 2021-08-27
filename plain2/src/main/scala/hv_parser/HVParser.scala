package hv_parser

object HVParser {

  type Header = String
  type Value = String

  /** It finds indexes of requested columns
    *
    * @param columns Iterable[String] with header names
    * @param interested Set[String] we are interested in
    *
    * @return (Header, Int) column name and its position
    */
  def findColumns(columns: Iterable[Header], interested: Set[Header]): Set[(Header, Int)] =
    columns
      .zipWithIndex
      .filter { case (column, _) => interested.contains(column) }
      .toSet

  /** pick data based on previous step (intermediate) */
  def pickHv(data: Vector[Value], found: Set[(Header, Int)]): Set[(Header, Value)] =
    found
      .map { case (header, index) => header -> data(index) }

  /** pick data based on previous step (intermediate) */
  def pickHvSorted(
      data: Vector[Value],
      found: Set[(Header, Int)],
      required: Map[Header, Int],
    ): Vector[(Header, Value)] =
    found
      .map { case (header, index) => (required(header), header -> data(index)) }
      .toVector
      .sortBy { case (idx, _) => idx }
      .map { case (_, data) => data }

  def pickVOnlySorted(data: Vector[Value], found: Set[(Header, Int)], required: Map[Header, Int]): Vector[Value] =
    pickHvSorted(data, found, required)
      .map { case (_, v) => v }

  /** First implementation based on the known size of headers length
    * TODO: set return type to Option
    * TODO: make it lazy in terms of headers
    * TODO: implement checking empty string as a sign for end of the stream
    *
    * @param headers All the headers we have
    * @param req All the headers we interested in
    * @return function
    */
  def createMapper(headers: Seq[Header], req: Seq[Header]): IndexedSeq[Value] => Seq[Value] =
    (data: IndexedSeq[Value]) => {

      /** required columns with their indexes */
      val required: Map[Header, Int] = req.zipWithIndex.toMap

      headers
        .zipWithIndex
        .filter { case (column, _) => required.contains(column) }
        .map { case (header, index) => (required(header), data(index)) }
        .toVector
        .sortBy { case (idx, _) => idx }
        .map { case (_, data) => data }
    }

}
