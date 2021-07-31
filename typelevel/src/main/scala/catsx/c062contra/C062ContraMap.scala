package catsx.c062contra

/**
  * the whole idea of contramap is:
  *
  * having F[B] and function A => B
  * we can derive F[A]
  *
  * example:
  * we know, how to print String, Int, Boolean, etc
  * but we don't know how to print Box[A]
  * if we specify function A => String
  * we will know how to print Box[A]
  *
  */
object C062ContraMap extends App {

  trait Printable[A] { self =>
    def format(a: A): String
    def contramap[B](f: B => A): Printable[B] = new Printable[B] {
      override def format(b: B): String = self.format(f(b))
    }
  }

  /** format anything by using implicit implementation */
  def formatAnything[A](value: A)(implicit pa: Printable[A]): String = pa.format(value)

  object primitiveInstances {
    implicit val str: Printable[String] = (value: String) => s"`$value`"
    implicit val bool: Printable[Boolean] = (value: Boolean) => if (value) "yes" else "no"
  }

  /** it uses java's toString method */
  object naiveImpl {
    implicit def boxNaive[A]: Printable[Box[A]] = (va: Box[A]) => s"${va.value}"
  }

  /** it uses already existing instances */
  object contramapImpl {
    implicit def boxContramap[A](implicit p: Printable[A]): Printable[Box[A]] = p.contramap[Box[A]](b => b.value)
  }

  object testPrimitive {
    import primitiveInstances._
    val s1: String = formatAnything("hello") // `hello`
    val s2: String = formatAnything(true)    // yes
  }

  object testDerivedNaive {
    import naiveImpl._
    val s3: String = formatAnything(Box(false)) // false
  }

  object testDerivedContramap {
    import primitiveInstances._
    import contramapImpl._
    val s4: String = formatAnything(Box("abc")) // `abc`
  }

  println(testPrimitive.s1)
  println(testPrimitive.s2)
  println(testDerivedNaive.s3)
  println(testDerivedContramap.s4)
}
