package ujson

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import os.Path

/**
  * uJson is uPickle's JSON library,
  * which can be used to easily manipulate JSON source and data structures 
  * without converting them into Scala case-classes
  */
class UJsonSpec extends AnyFunSpec with Matchers {
  describe("ujson") {
    val curPath: Path = os.pwd / "lihaoyi" / "src" / "main" / "scala" / "ujson"
    
    it("manipulating") {
      val json0: Arr = ujson.Arr(
        ujson.Obj("myFieldA" -> ujson.Num(1), "myFieldB" -> ujson.Str("g")),
        ujson.Obj("myFieldA" -> ujson.Num(2), "myFieldB" -> ujson.Str("k"))
      )
      val json: Arr = ujson.Arr( // The `ujson.Num` and `ujson.Str` calls are optional
        ujson.Obj("myFieldA" -> 1, "myFieldB" -> "g"),
        ujson.Obj("myFieldA" -> 2, "myFieldB" -> "k")
      )
      json0 shouldEqual json
      
    }
    
    it("reading") {
      // String
      val str =
        """
          |[{
          |  "myFieldA": 1,
          |  "myFieldB": "g"
          |},
          |{
          |  "myFieldA": 2,
          |  "myFieldB": "k"
          |}]
          |""".stripMargin
      // parsed to map
      val readed: Value = ujson.read(str)
      // access by keys
      readed(0)("myFieldA").num  shouldEqual 1
      readed(0)("myFieldB").str  shouldEqual "g"
      readed(1)("myFieldA").num  shouldEqual 2
      readed(1)("myFieldB").str  shouldEqual "k"
    }
    
    it("plain file reading") {
      val r: String = os.read(curPath / "ex1.json")
      r shouldEqual
        """{ "a": 1, "b": "hello" }
          |""".stripMargin 
    }
    
    it("plain reading and parsing") {
      val r: Value = ujson.read(os.read(curPath / "ex1.json"))
      r("a").numOpt shouldEqual Some(1)
      r("b").strOpt shouldEqual Some("hello")
      r.obj.keys.count(_ == "c") shouldEqual 0
    }
    
    it("plain reading and parsing nested") {
      val r: Value = ujson.read(os.read(curPath / "ex2.json"))
      r("a").numOpt shouldEqual Some(1)
      r("b").strOpt shouldEqual Some("hello")
      r("c").boolOpt shouldEqual Some(true)
      r("d")("auth").boolOpt shouldEqual Some(true)
      r("d")("key").numOpt shouldEqual Some(123)
      r("e")(0).num shouldEqual 11
      r("e")(1).num shouldEqual 22
      r("e")(2).num shouldEqual 33
    }
    
    it("reading array") {
            val json = ujson.read("""{"a":1}""")
//      val json = ujson.read("[11,22,33,4]")
      val ao = json.arrOpt
      ao should not be None
      ao.foreach(_.length should be >= 3)
//      val x: Option[ArrayBuffer[Value]] = 
//      pprint.log(json(0).num)//
      // shouldEqual 11
//      pprintln(json.arr)
//      import org.scalatest.OptionValues
//      val x: ArrayBuffer[Value] = a.value// shouldEqual 3
//      pprintln(x)
//      x.length shouldEqual 3
////      a.isDefined shouldEqual true
//      
    }
    
    it("2") {
      val myObj: Value = ujson.Arr(
        ujson.Obj("myFieldA"->1, "myFieldB"->"g"),
        ujson.Obj("myFieldA"->2, "myFieldB"->"k"),
      )
      
      val jsonExpected =
        """[{"myFieldA":1,"myFieldB":"g"},{"myFieldA":2,"myFieldB":"k"}]"""
      
      ujson.write(myObj) shouldEqual jsonExpected
    }
  }
}
