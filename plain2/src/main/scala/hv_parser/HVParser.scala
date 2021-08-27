package hv_parser

object HVParser {

  private val interestedIn = Map(
    "A" -> 1,
    "B" -> 2,
    "C" -> 3,
  )

  def find(columns: Iterable[String]): Set[(String, Int)] =
    columns
      .zipWithIndex
      .filter { case (column, _) => interestedIn.contains(column) }
      .toSet

  type Header = String
  type Value = String

  def pick(data: Vector[Value], meta: Set[(Header, Int)]): Set[(Header, Value)] =
    meta
      .map { case (header, index) => header -> data(index) }

  def pickSorted(data: Vector[Value], meta: Set[(Header, Int)]): Seq[(Header, Value)] =
    meta
      .map { case (header, index) => (interestedIn(header), header -> data(index)) }
      .toVector
      .sortBy { case (idx, _) => idx }
      .map { case (_, data) => data }

  def pickSortedData(data: Vector[Value], meta: Set[(Header, Int)]) =
    pickSorted(data, meta)
      .map { case (_, v) => v }

}
