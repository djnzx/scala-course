package x71cats

object C018PrintableEx extends App {

  // 1. interface definition
//  sealed // what the difference if trait is sealed and non-sealed ???
  trait Printable[A] {
    def formatq(a: A): String
  }

  // 2. implement interface for types we need
  object PrintableInstances {
    implicit val printableInt: Printable[Int] = new Printable[Int] {
      override def formatq(a: Int): String = a.toString
    }
    implicit val printableStr: Printable[String] = new Printable[String] {
      override def formatq(a: String): String = a
    }
  }

  // 3. Object which takes implicit implementation
  object Printable {
    def formatz[A](a: A)(implicit instance: Printable[A]): String = instance.formatq(a)
    def printz[A](a: A)(implicit instance: Printable[A]): Unit = println(formatz(a))
  }

  // U1. use without syntax
  import PrintableInstances._
  val s1: String = Printable.formatz(1)
  Printable.printz(1)
  val s2: String = Printable.formatz("abc")
  Printable.printz("abc")

  // 4. Syntax implementation
  object PrintableSyntax {
    implicit class PrintableOps[A](origin: A) {
      def formatx(implicit ev: Printable[A]): String = Printable.formatz(origin)
      def printx(implicit ev: Printable[A]): Unit = Printable.printz(origin)
      // these `ev: Printable[A]` called ev (evidence), will be passed to Printable.formatz()(...)
    }
  }

  import PrintableSyntax._

  // U2. use WITH syntax
  val s3 = 1.formatx
  val s4 = "abc".formatx
  val s5 = 3.5.formatx(_.toString)
  1.printx
  "abc".printx

  // victim :)
  final case class Cat(name: String, age: Int, color: String)
  object PrintableInstances2 {
    implicit val printableCat: Printable[Cat] = new Printable[Cat] {
      override def formatq(a: Cat): String = s"${a.name} is a ${a.age} year-old ${a.color} cat."
    }
  }
  val barcelona: Cat = Cat("Barcelona", 8, "Gray")
  import PrintableInstances2._

  val s6 = barcelona.formatx
  barcelona.printx





}
