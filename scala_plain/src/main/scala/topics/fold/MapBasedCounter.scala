package topics.fold

/**
  * map based counter
  */
object MapBasedCounter {

  val inc_fn: Option[Int] => Option[Int] = {
    case None => Some(1)
    case Some(x) => Some(x + 1)
  }
  val dec_fn: Option[Int] => Option[Int] = {
    case None | Some(1) => None
    case Some(x) => Some(x - 1)
  }

  def inc[A](map: Map[A, Int], key: A) = map.updatedWith(key)(inc_fn)

  def dec[A](map: Map[A, Int], key: A) = map.updatedWith(key)(dec_fn)

  val r = "Hello, World".toCharArray.foldLeft((Map.empty[Char, Int], List.empty[Char])) {
    case ((map, list), char) => (inc(map, char), char :: list)
  } match {
    case (map, list) => (map, list.reverse)
  }
}
