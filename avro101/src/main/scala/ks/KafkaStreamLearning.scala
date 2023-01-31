package ks

import org.apache.kafka.streams.kstream.ValueJoiner
import org.apache.kafka.streams.scala.ByteArrayKeyValueStore
import org.apache.kafka.streams.scala.kstream.{KTable, Materialized}

class JoinSignatureIfKeysSame[K, A, B](t1: KTable[K, A], t2: KTable[K, B]) {

  val x: KTable[K, (A, B)] = t1.leftJoin(t2)((a, b) => (a, b))

}

/** this: KTable[K, V]
  *
  * def join[VO, VR]        (other: KTable[K, VO])                                                                         (joiner: (V, VO) => VR): KTable[K, VR]
  * def join[VO, VR]        (other: KTable[K, VO],               materialized: Materialized[K, VR, ByteArrayKeyValueStore])(joiner: (V, VO) => VR): KTable[K, VR]
  * def join[VO, VR]        (other: KTable[K, VO], named: Named)                                                           (joiner: (V, VO) => VR): KTable[K, VR]
  * def join[VO, VR]        (other: KTable[K, VO], named: Named, materialized: Materialized[K, VR, ByteArrayKeyValueStore])(joiner: (V, VO) => VR): KTable[K, VR]
  *
  * def leftJoin[VO, VR]    (other: KTable[K, VO])                                                                         (joiner: (V, VO) => VR): KTable[K, VR]
  * def leftJoin[VO, VR]    (other: KTable[K, VO], named: Named)                                                           (joiner: (V, VO) => VR): KTable[K, VR]
  * def leftJoin[VO, VR]    (other: KTable[K, VO], named: Named, materialized: Materialized[K, VR, ByteArrayKeyValueStore])(joiner: (V, VO) => VR): KTable[K, VR]
  * def leftJoin[VO, VR]    (other: KTable[K, VO],               materialized: Materialized[K, VR, ByteArrayKeyValueStore])(joiner: (V, VO) => VR): KTable[K, VR]
  *
  *  + def leftJoin[VR, KO, VO](other: KTable[KO, VO], kx: Function[V, KO], joiner: ValueJoiner[V, VO, VR],                                  materialized: Materialized[K, VR, ByteArrayKeyValueStore]): KTable[K, VR]
  * def leftJoin[VR, KO, VO](other: KTable[KO, VO], kx: Function[V, KO], joiner: ValueJoiner[V, VO, VR], tableJoined: TableJoined[K, KO], materialized: Materialized[K, VR, ByteArrayKeyValueStore]): KTable[K, VR]
  * def leftJoin[VR, KO, VO](other: KTable[KO, VO], kx: Function[V, KO], joiner: ValueJoiner[V, VO, VR], named: Named,                    materialized: Materialized[K, VR, ByteArrayKeyValueStore]): KTable[K, VR]
  */
class JoinSignatureIfKeysDifferent[K1, K2, A, B, C](
    t1: KTable[K1, A],
    t2: KTable[K2, B],
    keyExtractor: A => K2,
    valueJoiner: ValueJoiner[A, B, C],
    materialized: Materialized[K1, C, ByteArrayKeyValueStore]
) {

  val r: KTable[K1, C] = t1.leftJoin(t2, keyExtractor, valueJoiner, materialized)

}
