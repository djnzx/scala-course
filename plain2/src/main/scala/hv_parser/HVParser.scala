package hv_parser

import scala.annotation.tailrec

object HVParser {

  type Header = String
  type Value = String
  type At = Int

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

  /** Second implementation based on the known size of headers length
    * TODO: set return type to Option
    * TODO: make it lazy in terms of headers
    * TODO: implement checking empty string as a sign for end of the stream
    *
    * @param headers All the headers we have
    * @param req All the headers we interested in
    * @return function
    */
  def createMapper2(headers: Seq[Header], req: Seq[Header]): Option[IndexedSeq[Value] => Seq[Value]] = {

    /** required columns with their indexes */
    val required: Map[Header, Int] = req.zipWithIndex.toMap

    val it = headers.iterator

    @tailrec
    def go(req: Set[Header], found: Map[Header, At], index: Int): Option[Map[Header, At]] = {

      /** we found everything */
      if (req.isEmpty) return Some(found)

      /** iterator exhausted, we don't have enough fields */
      if (it.isEmpty) return None

      /** next piece of data to check */
      val h = it.next

      /** we treat empty string - as exhausted iterator */
      if (h.isBlank) return None

      req.contains(h) match {
        /** needed, add */
        case true => go(req - h, found + (h -> index), index + 1)
        /** dont need */
        case false => go(req, found, index + 1)
      }
    }

    /** found headers collected */
    val found: Option[Map[Header, At]] = go(required.keySet, Map.empty, 0)

    found match {
      case Some(ff) =>
        Some { data: IndexedSeq[Value] =>
          ff.map { case (header, at) => (required(header), data(at)) }
            .toVector
            .sortBy { case (idx, _) => idx }
            .map { case (_, data) => data }
        }
      case _ => None
    }
  }

}
