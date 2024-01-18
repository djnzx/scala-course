package hackerrank.d200319

/**
  * https://www.hackerrank.com/challenges/counting-valleys/problem
  */
object CountingValleysScala extends App {

  case class State(level: Int, count: Int)

  def count(n: Int, s: String): Int =
    s.toArray.foldLeft(State(0,0)) { (st, c) =>
      val (prev, level) = (st.level, c match {
        case 'U' => st.level+1
        case 'D' => st.level-1
      })
      State(level, st.count + (if (level==0 && prev<0) 1 else 0))
//      if (level==0 && prev<0) State(level, st.count + 1)
//      else                    State(level, st.count)
    }.count
}
