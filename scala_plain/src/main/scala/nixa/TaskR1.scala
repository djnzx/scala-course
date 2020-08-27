package nixa

object TaskR1 extends App {

  /*
   * Complete the 'fetchItemsToDisplay' function below.
   *
   * The function is expected to return a STRING_ARRAY.
   * The function accepts following parameters:
   *  1. 2D_STRING_ARRAY items
   *  2. INTEGER sortParameter
   *  3. INTEGER sortOrder
   *  4. INTEGER itemsPerPage
   *  5. INTEGER pageNumber
   */

  def fetchItemsToDisplay(
                           items0: Array[Array[String]],
                           sortParameter: Int,
                           sortOrder: Int,
                           itemsPerPage: Int,
                           pageNumber: Int): Array[String] = {
    case class Item(name: String, rel: Int, price: Int)
    object Item {
      def from(line: Array[String]) = line match {
        case Array(name, rel, price) => Item(name, rel.toInt, price.toInt)
      }
    }
    val items: Array[Item] = items0.map(Item.from)
    ((sortParameter, sortOrder) match {
      case (0, 0) => items.sortBy(_.name)
      case (0, 1) => items.sortBy(_.name)(Ordering.String.reverse)
      case (1, 0) => items.sortBy(_.rel)
      case (1, 1) => items.sortBy(_.rel)(Ordering.Int.reverse)
      case (2, 0) => items.sortBy(_.price)
      case (2, 1) => items.sortBy(_.price)(Ordering.Int.reverse)
    })
    .slice(
      itemsPerPage * pageNumber - 1,
      itemsPerPage * pageNumber - 1 + itemsPerPage
    )
    .map(_.name)
  }

}
