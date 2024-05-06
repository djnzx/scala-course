package circe101.c3_manual_creation

import circe101.Base
import io.circe.Json

class JsonCombine extends Base {

  test("deep merge") {
    val json1 = Json.obj("a" -> Json.fromInt(1))
    val json2 = Json.obj("b" -> Json.fromInt(2))

    val json3 = json1 deepMerge json2

    pprint.log(json1)
    pprint.log(json2)
    pprint.log(json3)
  }

}
