package map_problems

import pprint.{pprintln => println}

object MapVsFunction extends App {

  type CommonCodesMap = Map[String, _]

  val commonCodes = Map(
    "a" -> 11,
    "b" -> 3.5,
    "c" -> "hello",
    "d" -> ("whatever", 3.14),
  )

  object Ugly {
    def extract1(cc: CommonCodesMap) = {
      cc.asInstanceOf[Map[String, Int]]("a")
    }

    def extract2(cc: CommonCodesMap) = {
      cc.asInstanceOf[Map[String, Double]]("b")
    }

    def extract3(cc: CommonCodesMap) = {
      cc.asInstanceOf[Map[String, String]]("c")
    }

    def extract4(cc: CommonCodesMap) = {
      cc.asInstanceOf[Map[String, (String, Double)]]("d")
    }

    val v1: Int = extract1(commonCodes)
    val v2: Double = extract2(commonCodes)
    val v3: String = extract3(commonCodes)
    val v4: (String, Double) = extract4(commonCodes)
  }
  
  object Good {
    def extract1(f: String => Int) = {
      f("a")
    }
    def extract2(f: String => Double) = {
      f("b")
    }
    def extract3(f: String => String) = {
      f("c")
    }
    def extract4(f: String => (String, String)) = {
      f("d")
    }

    val v1: Int = extract1(commonCodes.asInstanceOf[Map[String, Int]])
    val v2: Double = extract2(commonCodes.asInstanceOf[Map[String, Double]])
    val v3: String = extract3(commonCodes.asInstanceOf[Map[String, String]])
    val v4: (String, String) = extract4(commonCodes.asInstanceOf[Map[String, (String, String)]])
    
  }
  
  
}
