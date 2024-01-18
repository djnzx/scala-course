package hackerrankfp.d200421

/**
  * https://www.hackerrank.com/challenges/fp-list-replication/problem
  */
object ReplicateList extends App {

  def f(num:Int,arr:List[Int]):List[Int] =
    arr.flatMap(x => 1 to num map { _ => x })

}
