package aa_cookbook.x007

object Imports extends App {
  val l = List(1,2,3)
  // 1. Predef, java.lang._, scala._ automatically imported
  // import in any place
  // import java.util.{Liat1, List2}

  import java.util.{ArrayList => JArrayList}

  val a = new JArrayList[Int]()
  a.add(1)
  // hiding class
  import java.util.{Random => _}




}
