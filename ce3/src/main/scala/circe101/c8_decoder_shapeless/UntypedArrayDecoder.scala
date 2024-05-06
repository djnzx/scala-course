package circe101.c8_decoder_shapeless

import circe101.Base
import io.circe.parser._

class UntypedArrayDecoder extends Base {

  test("1") {
    val parsed = parse(Data.raw)
      .flatMap(
        _.hcursor
          .downField("rows")
          .as[List[Row]]
      )

    inside(parsed){
      case Right(xs) => xs.foreach(x => pprint.log(x))
    }
  }

}
