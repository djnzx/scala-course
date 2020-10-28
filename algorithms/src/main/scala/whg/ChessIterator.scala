package whg

import com.whitehatgaming._

class ChessIterator private (fileName: String) extends Iterator[Array[Int]] {
  private val ui: UserInput = new UserInputFile(fileName)
  private var line: Array[Int] = _

  override def hasNext: Boolean = {
    line = ui.nextMove()
    line != null
  }

  /**
    * abcdefgh => 01234567
    * 12345678 => 76543210
    */
  override def next(): Array[Int] = line
}

object ChessIterator {
  import Utils.{obtainResource, wrong}

  /** file - any location */
  def file(fileName: String) = new ChessIterator(fileName)
  
  /** file - located in the resources folder */
  def resource(fileName: String) = {
    val r = obtainResource(fileName).getOrElse(wrong(msg.fileNotFound(fileName))) 
    new ChessIterator(r)
  }

}
