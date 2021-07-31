package catsx.c000representation

import catsx.c000representation.C010Json.{JsonWriter, OptionDerivation3PatternMatch}
import catsx.c000representation.C010Json.JsonWriterInstances.personWriter

object C011AccessingImplicitInstances {

  val instance: JsonWriter[Person] = implicitly[JsonWriter[Person]]
  implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] = OptionDerivation3PatternMatch.optionWriter[A]

}
