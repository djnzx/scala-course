package _udemy.scala_beginners.lectures.part3fp._positions

object ThisIsTheTestStringV1 extends App {
  val origin = "This is the test string"
  val withIndex = origin.zipWithIndex
  // Vector((T,0), (h,1), (i,2), (s,3), ( ,4), (i,5), (s,6), ( ,7), (t,8), (h,9), (e,10), ( ,11), (t,12), (e,13), (s,14), (t,15), ( ,16), (s,17), (t,18), (r,19), (i,20), (n,21), (g,22))
  val filtered = withIndex.filter{ case (c, _) => c.isLetter }
  // Vector((T,0), (h,1), (i,2), (s,3), (i,5), (s,6), (t,8), (h,9), (e,10), (t,12), (e,13), (s,14), (t,15), (s,17), (t,18), (r,19), (i,20), (n,21), (g,22))
  //def grouper_func(t: (Char, Int)): Char = t._1
  val grouped = filtered.groupBy(t => t._1)
  // Map(e -> Vector((e,10), (e,13)), s -> Vector((s,3), (s,6), (s,14), (s,17)), n -> Vector((n,21)), T -> Vector((T,0))
  //def value_conv_function(l: IndexedSeq[(Char, Int)]): IndexedSeq[Int] = l.map(_._2)
  val cleaned1 = grouped.mapValues(el => el.map(_._2))
  // Map(e -> Vector(10, 13), s -> Vector(3, 6, 14, 17), n -> Vector(21), T -> Vector(0), t -> Vector(8, 12, 15, 18), i -> Vector(2, 5, 20), g -> Vector(22), h -> Vector(1, 9), r -> Vector(19))
  val cleaned2 = cleaned1.mapValues(_.mkString("[",",","]"))
  // Map(e -> [10,13], s -> [3,6,14,17], n -> [21], T -> [0], t -> [8,12,15,18], i -> [2,5,20], g -> [22], h -> [1,9], r -> [19])
  val list = cleaned2.toList
  // List((e,[10,13]), (s,[3,6,14,17]), (n,[21]), (T,[0]), (t,[8,12,15,18]), (i,[2,5,20]), (g,[22]), (h,[1,9]), (r,[19]))
  val list2 = list.map(t => s"${t._1}:${t._2}")
  // List(e:[10,13], s:[3,6,14,17], n:[21], T:[0], t:[8,12,15,18], i:[2,5,20], g:[22], h:[1,9], r:[19])
  val list3 = list2.mkString(", ")
  // e:[10,13], s:[3,6,14,17], n:[21], T:[0], t:[8,12,15,18], i:[2,5,20], g:[22], h:[1,9], r:[19]
  println(list3)
}
