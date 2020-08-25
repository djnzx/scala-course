package nixa

object Task2 {

  /*
   * Complete the 'findNumber' function below.
   *
   * The function is expected to return a STRING.
   * The function accepts following parameters:
   *  1. INTEGER_ARRAY arr
   *  2. INTEGER k
   */

  def oddNumbers(l: Int, r: Int): Array[Int] =
    (l to r)
      .filter(_ % 2 != 0)
      .toArray  
  
}