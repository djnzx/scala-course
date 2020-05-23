package essential

import java.time.LocalTime
import java.time.format.DateTimeFormatter

object X227TypeClass extends App {
  trait HtmlWriteable {
    def toHtml: String
  }

  // way #1, the same as in Java
  final case class Person(name: String, email: String)
    extends HtmlWriteable {
    def toHtml = s"<span>$name &lt;$email&gt;</span>"
  }

  // way #2,
  // - the everything must be coded before,
  // - we lost type safety!
  // - new type added - we must modify our code
  def toHtml2(in: Any): String = in match {
    case p:Person => s"Person:[${p.name.toUpperCase}]"
    case _ => s"n/a"
  }

  // way3. implicit class
  implicit class HtmlPerson(p: Person) {
    def html3: String = s"Person:<${p.name}>"
  }

  // way4. type class !
  trait Htmlable[A] {
    def toHtml4(in: A): String
  }

  // way 4.1 usage
  object PersonWriter extends Htmlable[Person] {
    override def toHtml4(in: Person): String = s"Person:[${in.name}]"
  }

  val df: LocalTime => String = lt => lt.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

  // we can add same behavior to already created classes
  object DateWriter extends Htmlable[LocalTime] {
    override def toHtml4(in: LocalTime): String = s"Time:[${df(in)}]"
  }

  // way 5.
  // step1. this is implicit implementation to wire to implicit class in compile time
  implicit val htmlablePerson: Htmlable[Person] = (in: Person) => s"Person[[${in.name.toUpperCase()}]]"

  // step2. this is implicit behavior extension
  implicit class HtmlablePerson(p: Person) {
    def html5(implicit instance: Htmlable[Person]): String = instance.toHtml4(p)
  }
  // step2.A this is implicit behavior extension to any class with appropriate implicit implementation
  // having that we can get rid of lines 53-55
  implicit class HtmlableAny[A](p: A) {
    def html5(implicit instance: Htmlable[A]): String = instance.toHtml4(p)
  }

  def htmlfy[A](data: A)(implicit instance: Htmlable[A]): String = instance.toHtml4(data)

  val p: Person = Person("alex","a77x77@gmail.com")
  println(p.toHtml)                             // directly implement(extends) HtmlWriteable
  println(toHtml2(p))                           // match/case pattern               (non type-safe)
  println(p.html3)                              // implicit class to only one class (untidy if many classes)
  println(PersonWriter.toHtml4(p))              // create object and use it         (untidy if many classes)
  println(htmlablePerson.toHtml4(p))            // calling implementation by instance (Java approach)
  println(p.html5)    // 53-55 or 58-60 for any // put implicit implementation into scope and use it!
                                                // appropriate instance will be chosen in compile time
  println(htmlfy(p))                            // the same approach but in function way, not infix

  //  experiments with different data type
  val lt: LocalTime = LocalTime.now()
  println(DateWriter.toHtml4(lt))
  // to use it we need only put into scope:
  implicit val htmlableDate: Htmlable[LocalTime] = (in: LocalTime) => s"TIME[[${df(in)}]]"
  println(htmlfy(lt))                           // because 62 and 78
  println(lt.html5)                             // because of line 58-60 and 78

  // and we can just pick implementation if we need it
  object HtmlableInstanceByVar {
    def apply[A](p: A)(implicit instance: Htmlable[A]): Htmlable[A] = instance
  }
  object HtmlableInstanceByType {
    def apply[A](implicit instance: Htmlable[A]): Htmlable[A] = instance
  }
  // pick instance by explicitly asking type
  val inst1: Htmlable[Person] = implicitly[Htmlable[Person]]
  // pick instance via inference from variable
  val inst2: Htmlable[Person] = HtmlableInstanceByVar(p)
  // pick instance via looking for implicits in the scope
  val inst3: Htmlable[Person] = HtmlableInstanceByType[Person]
  // use it !
  val s1: String = inst1.toHtml4(p)
  val s2: String = inst2.toHtml4(p)
  val s3: String = inst3.toHtml4(p)
  println(s1)
  println(s2)
  println(s3)
}
