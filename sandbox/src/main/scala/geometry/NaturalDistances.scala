package geometry

import common.Base

class NaturalDistances extends Base with GeometryFundamentals {

  def range(min: Int, count: Int) = LazyList.from(min).take(count)

  /** triplets with natural distances */
  def genNatTriplets(min: Int = 0, count: Int = 100) = {
    val r = range(min, count)
    r.flatMap { d1 =>
      r.flatMap { d2 =>
        r.flatMap { d3 =>
          distanceIsNat(d1, d2, d3)
            .filter(_ < 30) // filter distance
            .map(d => (d1, d2, d3) -> d)
        }
      }
    }
  }

  test("triplets") {
    genNatTriplets()
      .foreach(x => println(x))
  }

  test("isNat") {
    Seq(3.123, 3.987, 3.0)
      .foreach(x => pprint.log(isNat(x)))
  }

}
