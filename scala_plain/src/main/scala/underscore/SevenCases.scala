package underscore

object SevenCases {
  
  /** 1. ignore something */
  val _ = 5
  val (x, _) = (3, 5)
  (1 to 10).map(_ => 5)
  
  trait Singer
  trait Actor1 { self => // thia
  }
  trait Actor2 { _: Singer => // this
  }
  
  def process(list: List[Option[_]]): Int = list.length

  /** 2. everything */
  val meaning: Any = 42
  meaning match {
    case _ => "I's ok" 
  }
  import scala.concurrent.duration._
  
  /** 3. default */
  var s: String = _
  
  /** 4. lambda sugar */
  List(1,2,3).map(_ + 1)
  
  /** 5. Eta-expansion */
  def inc(x: Int) = x + 1
  val inc_func = inc _
  
  /** 6. HKT */
  class Jewel[M[_]]

  /** 7. VarArgs */
  def makeSentence(words: String*) = words.mkString(" ")
  makeSentence("I", "Love", "Scala")
  val word = Seq("I", "Love", "Scala")
  makeSentence(word: _*)
}
