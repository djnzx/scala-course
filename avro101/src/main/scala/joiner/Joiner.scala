package joiner

import org.apache.kafka.streams.scala.ByteArrayKeyValueStore
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams.scala.kstream.Materialized

/** this trait will be used
  * to join TWO arbitrary streams Iterable[A], Iterable[B]
  *
  * as the first implementations
  * it should work with the KafkaStreams
  * and MockedStreams being read from sequence of Json files to be able to run tests
  *
  * naturally, join operation can produce
  * 0, 1, or more records as a result
  * that's why result is always Iterable (Option)
  *
  * this: KTable[K, V]
  * def leftJoin[VR, KO, VO](
  *    other: KTable[KO, VO],
  *    kx: Function[V, KO],
  *    joiner: ValueJoiner[V, VO, VR],
  *    materialized: Materialized[K, VR, ByteArrayKeyValueStore]
  * ): KTable[K, VR]
  */
trait AgnosticJoiner[AS[_, _], K1, V3] {

  def join2[K2, V1, V2](
      keyExtractor: V1 => K2,
      combiner: (V1, V2) => V3
  )(
      as: AS[K1, V1],
      bs: AS[K2, V2]
  ): AS[K1, V3]

}

case class KTableT[K, V](items: Iterable[(K, V)])

object AgnosticJoiner {

  def join[AS[_, _], K1, V1, K2, V2, V3](
      keyExtractor: V1 => K2,
      combiner: (V1, V2) => V3
  )(
      as: AS[K1, V1],
      bs: AS[K2, V2]
  )(impl: AgnosticJoiner[AS, K1, V3]): AS[K1, V3] =
    impl.join2(keyExtractor, combiner)(as, bs)

}

object design1 {

  type KMaterialized[K1, V3] = Materialized[K1, V3, ByteArrayKeyValueStore]

  class KafkaStreamsJoiner[K1, V3](materialized: KMaterialized[K1, V3]) extends AgnosticJoiner[KTable, K1, V3] {
    override def join2[K2, V1, V2](keyExtractor: V1 => K2, combiner: (V1, V2) => V3)(
        as: KTable[K1, V1],
        bs: KTable[K2, V2]
    ): KTable[K1, V3] = as.leftJoin(bs, keyExtractor, combiner(_, _), materialized)
  }

  class IterableJoiner[K1, V3] extends AgnosticJoiner[KTableT, K1, V3] {
    override def join2[K2, V1, V2](keyExtractor: V1 => K2, combiner: (V1, V2) => V3)(
        as: KTableT[K1, V1],
        bs: KTableT[K2, V2]
    ): KTableT[K1, V3] = KTableT(
      as.items.flatMap { case (k1, v1) =>
        bs.items
          .filter { case (k2, _) => k2 == keyExtractor(v1) }
          .map { case (_, v2) => k1 -> combiner(v1, v2) }
      }
    )
  }

}

// TODO: the idea is to have the almost the same signature for KStream and MockedOne

/** in production we work with Kafka KTable[K1, V1]
  * we join KTable[K2, V2]
  * we have a function V1 => K2
  * we have a function (V1, V2) => V3
  * as a result we have KTable[K1, V3]
  *
  * generic representation could be:
  * --------------------------------
  * keyExtractor: V1 => K2
  * combiner: (V1, V2) => V3
  * data
  * - AS[K1, V1]
  * - AS[K2, V2]
  * result
  * - AS[K1, V3]
  *
  * runtime representation could be:
  * --------------------------------
  * keyExtractor: V1 => K2
  * combiner: (V1, V2) => V3
  * data
  * - KTable[K1, V1]
  * - KTable[K2, V2]
  * result
  * KTable[K1, V3]
  *
  * test representation could be:
  * -----------------------------
  * keyExtractor: V1 => K2
  * combiner: (V1, V2) => V3
  * data
  * - KTableT[K1, V1] (kind of iterable)
  * - KTableT[K2, V2] (kind of iterable)
  * result
  * KTableT[K1, V3]
  *
  * KafkaJoiner extends AgnosticJoiner[KTable]
  * TestJoiner extends AgnosticJoiner[KTableT]
  *
  * val jk = new KafkaJoiner(materializer)
  * val jt = new TestJoiner()
  *
  * jk.join(
  * keyExtractor: V1 => K2
  * combiner: (V1, V2) => V3
  * )(
  * table1: KTable[K1, V1]
  * table2: KTable[K2, V2]
  * ) => KTable[K1, V3]
  *
  * jt.join(
  * keyExtractor: V1 => K2
  * combiner: (V1, V2) => V3
  * )(
  * table1: KTableT[K1, V1]
  * table2: KTableT[K2, V2]
  * ) => KTableT[K1, V3]
  *
  * generic signature for method should be
  *
  * def join(
  * keyExtractor: V1 => K2
  * combiner: (V1, V2) => V3
  * )(
  * table1: AS[K1, V1]
  * table2: AS[K2, V2]
  * ) => AS[K1, V3]
  */

object data_test {
  //               k1          v1
  val t1: KTableT[Int, (String, Long)] = new KTableT(
    Vector(
      (1, ("test 1", 101L)),
      (2, ("test 2", 102L)),
      (3, ("test 3", 103L))
    )
  )

  //               k2          v2
  val t2: KTableT[Long, (String, Double)] = new KTableT(
    Vector(
      (101L, ("RED", 1001.1)),
      (102L, ("GREEN", 1002.2)),
      (103L, ("BLUE", 1003.3))
    )
  )
}

object data_real {
  //               k1          v1
  val t1: KTable[Int, (String, Long)] = ???
  val t2: KTable[Long, (String, Double)] = ???
}

object design2 extends App {
  import design1._

  lazy val kMaterialized: KMaterialized[Int, (String, String, Double)] = ???
  lazy val ksJoiner: AgnosticJoiner[KTable, Int, (String, String, Double)] = new KafkaStreamsJoiner(kMaterialized)
  val tiJoiner: AgnosticJoiner[KTableT, Int, (String, String, Double)] = new IterableJoiner[Int, (String, String, Double)]

  val keyExtractor: ((String, Long)) => Long =
    (v1: (String, Long)) => v1._2

  val valueCombiner: ((String, Long), (String, Double)) => (String, String, Double) =
    (v1: (String, Long), v2: (String, Double)) => (v1._1, v2._1, v2._2)


  object example_test {
    import data_test._
    val joined = AgnosticJoiner.join(keyExtractor, valueCombiner)(t1, t2)(tiJoiner)
    joined.items.foreach(println)
  }

  object example_real {
    import data_real._
    val joined = AgnosticJoiner.join(keyExtractor, valueCombiner)(t1, t2)(ksJoiner)
  }


}
