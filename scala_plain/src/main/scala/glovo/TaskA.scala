package glovo

/**
  * Binomial coefficient
  * 
  *               N !         N (N - 1) ... (K + 1)
  * C(N, K) = ------------- = ---------------------
  *           (N - K)! * K!           (N - K) !
  */
object TaskA {

  type BD = java.math.BigDecimal
  val M1 = new BD(1_000_000_000)
  val ONE = new BD(1)
  val ERR = -1
  
  def mul(a: BD, b: Int) = a.multiply(new BD(b))
  def fact(n: Int, r: BD = ONE): BD = if (n == 0) r else fact(n - 1, mul(r, n))
  def isValidResult(n: BD) = n.compareTo(M1) <= 0
  def nUptoK(n: Int, k: Int) = ((k + 1) to n).foldLeft(ONE)(mul)
  def value(n: Int, k: Int) = nUptoK(n, k).divide(fact(n-k))
  def isValidInput(n: Int, k: Int) = !(n < 0 || k < 0 || n < k)

  def solution(n: Int, k: Int) =
    if (!isValidInput(n, k)) ERR
    else Some(value(n, k))
      .filter(isValidResult)
      .map(_.intValue)
      .getOrElse(ERR)

}
