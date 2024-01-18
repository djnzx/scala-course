package hackerrank.d200320.cavitymap

/**
  * https://www.hackerrank.com/challenges/cavity-map/problem
  */
object CavityMapV3 extends App {

  def cavityMap(grid: Array[String]) = {

    def isCavityAt(x: Int, y: Int) = x > 0 && y > 0 &&
      y < grid.length - 1 && x < grid(y).length - 1 &&
      grid(y)(x) > grid(y)(x-1) &&
      grid(y)(x) > grid(y-1)(x) &&
      grid(y)(x) > grid(y+1)(x) &&
      grid(y)(x) > grid(y)(x+1)

    def solve(x: Int, y: Int) = if (isCavityAt(x, y)) 'X' else grid(y)(x)

    grid.indices map { y =>
      grid(y).indices map { solve(_, y) } mkString
    } toArray
  }

}
