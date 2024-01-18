package hackerrank.d200320.team

/**
  * https://www.hackerrank.com/challenges/acm-icpc-team/problem
  */
object ACMICPCTeamAppScala extends App {

  def acmTeam(topics: Array[String]) = {
    val len = topics.length
    val data = (0 until len) flatMap { i1 =>
      (i1+1 until len) map { i2 =>
        topics(i1) zip topics(i2) map { t =>
          t._1 == '1' || t._2 == '1' } count { _ == true }
      }
    } filter { _ > 0 }

    val maxT = data.maxOption getOrElse 0
    val maxG = data.count { _ == maxT }
    Array(maxT, maxG)
  }

}
