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

  def join[K2, V1, V2](
      keyExtractor: V1 => K2
  )(
      combiner: (V1, V2) => V3
  )(
      as: AS[K1, V1],
      bs: AS[K2, V2]
  ): AS[K1, V3]

}
// TODO: the idea is to have the almost the same signature for KStream and MockedOne
/** joiner for kafka stream */
class KTableJoiner[K1, V3](materialized: Materialized[K1, V3, ByteArrayKeyValueStore]) extends AgnosticJoiner[KTable, K1, V3] {

  override def join[K2, V1, V2](
      keyExtractor: V1 => K2
  )(
      combiner: (V1, V2) => V3
  )(
      as: KTable[K1, V1],
      bs: KTable[K2, V2]
  ): KTable[K1, V3] =
    as.leftJoin(
      bs,
      keyExtractor,
      combiner(_, _),
      materialized
    )

}

class HowToUse[K1, K2, V1, V2, V3](
    m: Materialized[K1, V3, ByteArrayKeyValueStore],
    keyExtractor: V1 => K2,
    combiner: (V1, V2) => V3
) {
  val joiner: AgnosticJoiner[KTable, K1, V3] = new KTableJoiner(m)
  val joinerFn: (KTable[K1, V1], KTable[K2, V2]) => KTable[K1, V3] = joiner.join(keyExtractor)(combiner)
}
