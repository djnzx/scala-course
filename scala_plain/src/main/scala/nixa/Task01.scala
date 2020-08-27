package nixa

object Task01 {

  /*
   * Complete the 'findNumber' function below.
   *
   * The function is expected to return a STRING.
   * The function accepts following parameters:
   *  1. INTEGER_ARRAY arr
   *  2. INTEGER k
   */

  def findNumber(arr: Array[Int], k: Int): String =
    if (arr.contains(k)) "YES" else "NO"
  
}