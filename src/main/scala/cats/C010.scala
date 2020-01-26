package cats

object C010 extends App {
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

  // our victim :)
  final case class Person(name: String, email: String)

  // 2. implement our JsonWriter
  object JsonWriterInstances {
    implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
      override def write(value: String): Json = JsString(value)
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

  // 3.
  object Json {
    def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)
  }

  import JsonWriterInstances._


  val s: Json = Json.toJson(Person("Dave", "dave@example.com"))

  // 4.
  object JsonSyntax {
    implicit class JsonWriterOps[A](value: A) {
      def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
    }
  }

  import JsonSyntax._
  val s2: Json = Person("Dave", "dave@example.com").toJson(personWriter)
  val s3: Json = Person("Dave", "dave@example.com").toJson

  // pick an instance in runtime
  val instance: JsonWriter[Person] = implicitly[JsonWriter[Person]]

  // full syntax
  def optionWriter1[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
    new JsonWriter[Option[A]] {
      def write(option: Option[A]): Json =
        option match {
          case Some(aValue) => writer.write(aValue) case None => JsNull
        }
    }

  // anonymous class syntax
  def optionWriter2[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
    (option: Option[A]) => option match {
      case Some(aValue) => writer.write(aValue)
      case None => JsNull
    }

  // anonymous match syntax
  def optionWriter3[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] = {
      case Some(aValue) => writer.write(aValue)
      case None => JsNull
    }

  implicit def optionWriter[A](implicit writer: JsonWriter[A]): JsonWriter[Option[A]] = optionWriter3[A]
}
