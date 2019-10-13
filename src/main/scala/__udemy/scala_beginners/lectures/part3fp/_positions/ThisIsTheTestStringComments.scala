package __udemy.scala_beginners.lectures.part3fp._positions

object ThisIsTheTestStringComments extends App {
  "This is the test string"
      .zipWithIndex // Vector((T,0), (h,1), (i,2), (s,3), ( ,4), (i,5), (s,6), ( ,7), (t,8), (h,9), (e,10), ( ,11), (t,12), (e,13), (s,14), (t,15), ( ,16), (s,17), (t,18), (r,19), (i,20), (n,21), (g,22))
      .filter{ case (c, _) => c.isLetter } // Vector((T,0), (h,1), (i,2), (s,3), (i,5), (s,6), (t,8), (h,9), (e,10), (t,12), (e,13), (s,14), (t,15), (s,17), (t,18), (r,19), (i,20), (n,21), (g,22))
      .groupBy(t => t._1) // Map(e -> Vector((e,10), (e,13)), s -> Vector((s,3), (s,6), (s,14), (s,17)), n -> Vector((n,21)), T -> Vector((T,0))
      .mapValues(el => el.map(_._2)) // Map(e -> Vector(10, 13), s -> Vector(3, 6, 14, 17), n -> Vector(21), T -> Vector(0), t -> Vector(8, 12, 15, 18), i -> Vector(2, 5, 20), g -> Vector(22), h -> Vector(1, 9), r -> Vector(19))
      .mapValues(_.mkString("[",".","]")) // Map(e -> [10,13], s -> [3,6,14,17], n -> [21], T -> [0], t -> [8,12,15,18], i -> [2,5,20], g -> [22], h -> [1,9], r -> [19])
      .toList // List((e,[10,13]), (s,[3,6,14,17]), (n,[21]), (T,[0]), (t,[8,12,15,18]), (i,[2,5,20]), (g,[22]), (h,[1,9]), (r,[19]))
      .sortBy(t => t._1) // (T,[0])(e,[10.13])(g,[22])(h,[1.9])(i,[2.5.20])(n,[21])(r,[19])(s,[3.6.14.17])(t,[8.12.15.18])
      .map(t => s"${t._1}:${t._2}") // T:[0]e:[10.13]g:[22]h:[1.9]i:[2.5.20]n:[21]r:[19]s:[3.6.14.17]t:[8.12.15.18]
      .mkString(", ") // T:[0], e:[10.13], g:[22], h:[1.9], i:[2.5.20], n:[21], r:[19], s:[3.6.14.17], t:[8.12.15.18]
      .foreach(print)
}
