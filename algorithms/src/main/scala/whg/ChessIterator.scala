package whg

import com.whitehatgaming._

class ChessIterator(fileName: String) extends Iterator[Array[Int]] {
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
  
  def resource(fileName: String): ChessIterator = 
    new ChessIterator(this.getClass.getClassLoader.getResource(fileName).getFile)
    
}