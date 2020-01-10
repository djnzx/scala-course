package _degoes.hkt5

object HKTApp5 extends App {

  trait Get[A] {
    def get(k: String): A
  }

  trait Put[A] {
    def put(k: String, v: A): Unit
  }

  trait Store[A] extends Get[A] with Put[A]

  class KVStoreM[A] extends Store[A] {
    val store: scala.collection.mutable.Map[String, A] = scala.collection.mutable.Map[String, A]()

    override def get(k: String): A = store(k)
    override def put(k: String, v: A): Unit = store.put(k, v)
  }

  class KVStoreIM[A] extends Store[A] {
    var store: Map[String, A] = Map[String, A]()

    override def get(k: String): A = store(k)
    override def put(k: String, v: A): Unit = { store = store + ( k->v ) }
  }

  implicit val s1: Store[String] = new KVStoreM[String]
  implicit val s2: Store[String] = new KVStoreIM[String]
  s1.put("A","A1")
  s1.put("B","B1")
  s2.put("A","A2")
  s2.put("B","B2")

  println(s1.get("A"))
  println(s1.get("B"))
  println(s2.get("A"))
  println(s2.get("B"))

  def put[A](k: String, v: A)(implicit s: Store[A]): Unit = {
  }

  put("C", "C1")(s1)

}
