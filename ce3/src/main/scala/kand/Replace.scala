package kand

import org.scalatest.funsuite.AnyFunSuite

class Replace extends AnyFunSuite {

  val m = Map(
    1   -> List(45, 67),
    2   -> List(125, 115),
//    3   -> List(83, 108, 136),
    3   -> List(108, 136),
    4   -> List(74, 84),
    5   -> List(129),
    6   -> List(121, 122),
    7   -> List(94, 110),
    8   -> List(63, 106),
    9   -> List(41, 64),
    10  -> List(47, 38),
    11  -> List(107, 85, 43),
    12  -> List(55, 72),
    13  -> List(49),
    14  -> List(117, 98),
    15  -> List(68, 146),
    16  -> List(95, 144),
//    17  -> List(57, 58),
    17  -> List(57),
    18  -> List(56),
    19  -> List(127, 118),
    20  -> List(130),
    56  -> List(20),
    21  -> List(54),
//    30  -> List(100, 141),
    30  -> List(141),
//    31  -> List(101, 90),
    31  -> List(90),
    34  -> List(8),
    58  -> List(70),
//    59  -> List(142, 133, 111),
    59  -> List(133, 111),
    61  -> List(140),
    62  -> List(71),
    63  -> List(44),
    64  -> List(53, 116),
    66  -> List(92, 75),
    67  -> List(103),
    68  -> List(46),
    69  -> List(114),
    70  -> List(65),
    71  -> List(147, 119, 99),
    72  -> List(61),
    73  -> List(135),
    76  -> List(134, 88, 97, 139),
//    79  -> List(96, 42, 73, 86),
    79  -> List(73, 86),
//    80  -> List(81, 78),
    80  -> List(78),
    81  -> List(105),
    83  -> List(112, 113),
    85  -> List(131, 148),
//    86  -> List(87, 149, 93, 102),
    86  -> List(93, 102),
    87  -> List(66, 40),
    88  -> List(50, 132),
    89  -> List(104, 91),
    90  -> List(59, 80),
    91  -> List(52),
    92  -> List(89, 120),
    94  -> List(123),
//    95  -> List(79, 48, 76, 145),
    95  -> List(76, 145),
    105 -> List(124),
    106 -> List(69),
//    107 -> List(143, 126, 62),
    107 -> List(126, 62),
    109 -> List(51),
    110 -> List(60, 109, 39),
    114 -> List(82),
  )

  val extra = Set('[', ']', ' ')

  def replace(raw: String) =
    raw.filterNot(extra.contains)
      .split(",")
      .map(_.toInt)
      .map(x => m(x))
      .map(_.mkString(", "))
      .mkString("[",",\n", "]")

  test("go") {
    val x2 = replace("18, 19, 11")
    println(x2)
  }

}
