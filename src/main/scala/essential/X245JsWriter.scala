package essential

import java.util.Date

object X245JsWriter extends App {
  // given 1. our data structure (ADT)
  sealed trait Visitor {
    def id: String
    def createdAt: Date
    def age: Long = new Date().getTime - createdAt.getTime
  }
  final case class Anonymous(id: String,           createdAt: Date = new Date()) extends Visitor
  final case class User(id: String, email: String, createdAt: Date = new Date()) extends Visitor

  // given 2. json representation (ADT)
  sealed trait JsValue {
    def stringify: String
  }
  final case class JsObject(values: Map[String, JsValue]) extends JsValue {
    def stringify = values
      // base functional syntax
      .map( e =>  "\"" + e._1 + "\":" + e._2.stringify )
      // tuple to values destruct
//      .map { case (name, value) => "\"" + name + "\":" + value.stringify }
      .mkString("{", ",", "}")
  }
  final case class JsString(value: String) extends JsValue {
    def stringify = "\"" + value.replaceAll("\\|\"", "\\\\$1") + "\""
  }

  // our instances to work with
  val obj: JsValue = JsObject(Map(
    "foo" -> JsString("a"),
    "bar" -> JsString("b"),
    "baz" -> JsString("c")
  ))
  val vis1 = Anonymous("0000fc")
  val vis2 = User     ("0000cx", "a@b.c")

  // having that we can write for any instance of `JsValue`
  println(obj.stringify)

  // but we want to be able to convert ANY OUR data to json (ENRICH W/EXTRA BEHAVIOR)
  // step 1. create A TYPE CLASS
  trait JsWriter[A] {
    def toJsValue(v: A): JsValue
  }
  // step 2. put into scope implicit implementations we are using in our DATA (string, long, date)
  implicit val jsw_s: JsWriter[String] = (v: String) => JsString(v)
  implicit val jsw_l: JsWriter[Long] = (v: Long) => JsString(v.toString)
  implicit val jsw_d: JsWriter[Date] = (v: Date) => JsString(v.toString)
  // step 3, put into scope implicit class with our method: `toXJson` !
  // by implicit class attach to any type !
  // by implicit instance - pick appropriate implementation !
  implicit class AlmostAnyToJson[A](value: A) {
    def toXJson(implicit instance: JsWriter[A]): JsValue = instance.toJsValue(value)
  }
  // step 4. exact representation for our data !
  implicit val jsw2: JsWriter[Anonymous] = (v: Anonymous) => JsObject(Map(
    "id"       -> v.id.toXJson,        // this works because we have
    "createdAt"-> v.createdAt.toXJson, // JsWriter[String], JsWriter[Long], JsWriter[Date] implemented
    "age"      -> v.age.toXJson,       // and `implicit class AlmostAnyToJson[A](value: A)`
  ))
  implicit val jsw3: JsWriter[User] = (v: User) => JsObject(Map(
    "id"       -> v.id.toXJson,
    "email"    -> v.email.toXJson,
    "createdAt"-> v.createdAt.toXJson,
    "age"      -> v.age.toXJson,
  ))
  // step 5. because sometimes we want to work with `Anonymous` and `User` in the same way we need that
  implicit val jsw4: JsWriter[Visitor] = {
    case a: Anonymous => a.toXJson
    case u: User => u.toXJson
  }
  // step 6. use everything !

  val json1: String = obj.stringify
  val json2: String = vis1.toXJson.stringify
  val json3: String = vis2.toXJson.stringify
  val seq_visitors: Seq[Visitor] = Seq(
    Anonymous("001", new Date),
    User("003 ", "dave@xample.com", new Date)
  )
  val seq_jsv: Seq[JsValue] = seq_visitors.map(v => v.toXJson)
  val seq_strings: Seq[String] = seq_jsv.map(v => v.stringify)
  Anonymous("001", new Date).toXJson
}
