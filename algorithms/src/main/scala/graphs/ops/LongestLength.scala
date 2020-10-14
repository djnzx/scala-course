package graphs.ops

trait LongestLength {
  /**
    * @return tuple of:
    *         - path length
    *         - last vertex of the path. 
    */
  def longestLength: (Int, Int)
}
