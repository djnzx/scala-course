package ujson

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

/**
  * uJson is uPickle's JSON library,
  * which can be used to easily manipulate JSON source and data structures 
  * without converting them into Scala case-classes
  */
class UJsonSpec extends AnyFunSpec with Matchers {
  describe("ujson") {
    it("1") {
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
    it("2") {
      // String
      val str = """[{"myFieldA":1,"myFieldB":"g"},{"myFieldA":2,"myFieldB":"k"}]"""
      // parsed to map
      val json: Value = ujson.read(str)
      // access by keys
      json(0)("myFieldA").num  shouldEqual 1
      json(0)("myFieldB").str  shouldEqual "g"
      json(1)("myFieldA").num  shouldEqual 2
      json(1)("myFieldB").str  shouldEqual "k"
      ujson.write(json)        shouldEqual str
    }
  }
}
