package cats101.c000representation

object C010Json extends App {

  sealed trait Json
  final case class JsObject(get: Map[String, Json]) extends Json
  final case class JsString(get: String) extends Json
  final case class JsNumber(get: Double) extends Json
  final case class JsBoolean(get: Boolean) extends Json
  case object JsNull extends Json

  // 1. type-class
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  // 2. implement our JsonWriter
  object JsonWriterInstances {
    implicit val str: JsonWriter[String] = new JsonWriter[String] {
      override def write(value: String): Json = JsString(value)
    }
    implicit val int: JsonWriter[Int] = new JsonWriter[Int] {
      override def write(value: Int): Json = JsNumber(value)
    }
    implicit val double: JsonWriter[Double] = new JsonWriter[Double] {
      override def write(value: Double): Json = JsNumber(value)
    }
    implicit val bool: JsonWriter[Boolean] = new JsonWriter[Boolean] {
      override def write(value: Boolean): Json = JsBoolean(value)
    }

    // ...
    // all other instances
    // ...

    implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
      override def write(p: Person): Json = JsObject(
        Map(
          "name" -> JsString(p.name),
          "email" -> JsString(p.email),
        )
      )
    }
  }

  // 3. extracting implicit instance and using it
  object Json {
    def toJson[A](a: A)(implicit wa: JsonWriter[A]): Json = wa.write(a)
  }

  // 4. create syntax
  object JsonSyntax {
    implicit class JsonWriterOps[A](value: A) {
      def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
    }
  }

  // manual usage
  object example1 {
    val x1: Json = JsonWriterInstances.int.write(33)
    val x2: Json = JsonWriterInstances.str.write("hello")
    val x3: Json = JsonWriterInstances.personWriter.write(Person("Dave", "dave@example.com"))
  }

  // manual usage using imports
  object example2 {
    import JsonWriterInstances._

    val x1: Json = int.write(33)
    val x2: Json = str.write("hello")
    val x3: Json = personWriter.write(Person("Dave", "dave@example.com"))
  }

  // better syntax, using implicit instances
  object example3 {
    import JsonWriterInstances._

    val x1: Json = Json.toJson(33)
    val x3: Json = Json.toJson(Person("Dave", "dave@example.com"))
  }

  // attaching syntax
  object example4 {
    import JsonSyntax._

    val x1: Json = Person("Dave", "dave@example.com").toJson(JsonWriterInstances.personWriter)
  }

  // attaching syntax & instances
  object example5 {
    import JsonWriterInstances._
    import JsonSyntax._

    val x1: Json = Person("Dave", "dave@example.com").toJson
  }

  /**
    * dealing with Option:
    * knowing how to write A, we can derive, how to write Option[A]
    */
  object OptionDerivation1fullSyntax {
    def optionWriter[A](implicit wa: JsonWriter[A]): JsonWriter[Option[A]] =
      new JsonWriter[Option[A]] {
        def write(oa: Option[A]): Json =
          oa match {
            case Some(a) => wa.write(a)
            case None => JsNull
          }
      }
  }

  object OptionDerivation2SingleMethod {
    def optionWriter[A](implicit wa: JsonWriter[A]): JsonWriter[Option[A]] =
      (oa: Option[A]) => oa match {
        case Some(a) => wa.write(a)
        case None => JsNull
      }
  }

  object OptionDerivation3PatternMatch {
    def optionWriter[A](implicit wa: JsonWriter[A]): JsonWriter[Option[A]] = {
      case Some(a) => wa.write(a)
      case None => JsNull
    }
  }

}
