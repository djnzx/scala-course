package hackerrankfp.d200421_01.johnfence


object JohnFenceV5fold {
  def readLine = scala.io.StdIn.readLine()

  def width(idx: Int, s: List[Int]) = s match {
    case Nil    => idx
    case h :: _ => idx - 1 - h
  } 
  
  def calcFence(fence: Vector[Int]) = {

    def step1(i: Int, s: List[Int], prev: Int): (Int, List[Int], Int) =
      if (i < fence.length)
        s match {
          case Nil                         => step1(i+1, i::s, prev)
          case h::_ if fence(h) < fence(i) => step1(i+1, i::s, prev)
          case h::t  =>
            val ma2 = prev max fence(h) * width(i, t)
            step1(i, t, ma2)
        }
      else (i, s, prev)

    val (i, s, ma) = step1(0, List.empty, 0)

    val (_, _, ma3) = s.foldLeft((i, s, ma)) { case ((idx, l @ h :: t, prev), _) =>
      val ma2 = prev max fence(h) * width(idx, l)
      (idx, t, ma2)
    }
    
    ma3
  }

  def main(args: Array[String]) = {
    val _ = readLine
    val fence = readLine.split(" ").map(_.toInt).toVector
    val max = calcFence(fence)
    println(max)
  }

  def main_test1(args: Array[String]) = {
    val fence = Vector(1, 2, 3, 4, 5, 6, 5, 4, 3, 0, 4, 5, 6, 7, 8, 6, 4, 2)
    val max = calcFence(fence)
    println(max)
  }

  def main_test2(args: Array[String]) = {
      val src = scala.io.Source.fromFile(new java.io.File("scala_plain/src/main/scala/hackerrankfp/d200421_01/test2big"))
      val _ = src.getLines().take(1).next()
      val fence = src.getLines().map(_.trim).next().split(" ").map(_.toInt).toVector
    val max = calcFence(fence)
    println(max)
  }
}
