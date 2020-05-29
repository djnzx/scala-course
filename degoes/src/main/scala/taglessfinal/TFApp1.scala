package degoes.taglessfinal

/**
  * https://scalac.io/tagless-final-pattern-for-scala-code/
  * https://github.com/pjazdzewski1990/Tagless-final-blog
  *
  * how to make sure the programs we write are correct?
  *
  * Scala will be our hosting language
  *
  * 1. Language, that defines a subset of operations that the hosted language allows
  * 2. Bridges, helpers that express Scala values and business logic in the Language
  * 3. Interpreters, dual to Bridges they run logic expressed as the Language and get the final value
  */
object TFApp1 extends App {

  // our language
  trait Language[Wrapper[_]] {
    // number operations
    def number(v: Int): Wrapper[Int]
    def increment(a: Wrapper[Int]): Wrapper[Int]
    def add(a: Wrapper[Int], b: Wrapper[Int]): Wrapper[Int]

    // string operations
    def text(v: String): Wrapper[String]
    def toUpper(a: Wrapper[String]): Wrapper[String]
    def concat(a: Wrapper[String], b: Wrapper[String]): Wrapper[String]

    // converter from Int to String
    def toString(v: Wrapper[Int]): Wrapper[String]
  }
  // as a result - you cannot call toUpper on an Int

  // bridge between the `Scala` and `our Language`
  trait ScalaToLanguageBridge[ScalaValue] {
    def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[ScalaValue]
  }

  def buildNumber(number: Int): ScalaToLanguageBridge[Int] = new ScalaToLanguageBridge[Int] {
    override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = L.number(number)
  }
  val i1: ScalaToLanguageBridge[Int] = buildNumber(1)

  def buildIncrementNumber(number: Int) = new ScalaToLanguageBridge[Int] {
    override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = L.increment(L.number(number))
  }
  val i2: ScalaToLanguageBridge[Int] = buildIncrementNumber(2)
  // Our bridges are really simple.
  // They take a single Scala value number: Int and convert it into an expression in our language
  // Since we are operating only on given L we cannot represent incorrect logic

  def buildIncrementExpression(expression: ScalaToLanguageBridge[Int]) = new ScalaToLanguageBridge[Int] {
    override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = L.increment(expression.apply)
  }
  val i3: ScalaToLanguageBridge[Int] = buildIncrementExpression(i2)

  // builds an expression like: println(s"$text ${a + (b + 1)}")
  def buildComplexExpression(text: String, a: Int, b: Int) = new ScalaToLanguageBridge[String] {
    override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[String] = {
      val addition = L.add(L.number(a), L.increment(L.number(b)))
      L.concat(L.text(text), L.toString(addition))
    }
  }

  val fullExpression: ScalaToLanguageBridge[String] = buildComplexExpression("Result is", 10, 1)

  type NoWrap[A] = A
  // NoWrap[Int] = Int

  val interpret = new Language[NoWrap] {
    override def number(v: Int): NoWrap[Int] = v
    override def increment(a: NoWrap[Int]): NoWrap[Int] = a + 1
    override def add(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a + b

    override def text(v: String): NoWrap[String] = v
    override def toUpper(a: NoWrap[String]): NoWrap[String] = a.toUpperCase
    override def concat(a: NoWrap[String], b: NoWrap[String]): NoWrap[String] = a + " " + b

    override def toString(v: NoWrap[Int]): NoWrap[String] = v.toString
  }

  val r1: NoWrap[String] = fullExpression.apply(interpret)
  println(r1)

  type PrettyPrint[ScalaValue] = String
  val interpretAsPrettyPrint = new Language[PrettyPrint] {
    override def number(v: Int): PrettyPrint[Int] = s"($v)"
    override def increment(a: PrettyPrint[Int]): PrettyPrint[Int] = s"(inc $a)"
    override def add(a: PrettyPrint[Int], b: PrettyPrint[Int]): PrettyPrint[Int] = s"(+ $a $b)"

    override def text(v: String): PrettyPrint[String] = s"[$v]"
    override def toUpper(a: PrettyPrint[String]): PrettyPrint[String] = s"(toUpper $a)"
    override def concat(a: PrettyPrint[String], b: PrettyPrint[String]): PrettyPrint[String] = s"(concat $a $b)"

    override def toString(v: PrettyPrint[Int]): PrettyPrint[String] = s"(toString $v)"
  }

  val r2: PrettyPrint[String] = fullExpression.apply(interpretAsPrettyPrint)
  println(r2)

  trait LanguageWithMul[Wrapper[_]] extends Language[Wrapper] {
    def multiply(a: Wrapper[Int], b: Wrapper[Int]): Wrapper[Int]
  }

  trait ScalaToLanguageWithMulBridge[ScalaValue] {
    def apply[Wrapper[_]](implicit L: LanguageWithMul[Wrapper]): Wrapper[ScalaValue]
  }

  def multiply(a: Int, b: Int) = new ScalaToLanguageWithMulBridge[Int] {
    override def apply[Wrapper[_]](implicit L: LanguageWithMul[Wrapper]): Wrapper[Int] = {
      L.multiply(L.number(a), L.number(b))
    }
  }

  val interpretWithMul = new LanguageWithMul[NoWrap] {
    override def multiply(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a * b

    override def number(v: Int): NoWrap[Int] = v
    override def increment(a: NoWrap[Int]): NoWrap[Int] = a + 1
    override def add(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a + b

    override def text(v: String): NoWrap[String] = v
    override def toUpper(a: NoWrap[String]): NoWrap[String] = a.toUpperCase
    override def concat(a: NoWrap[String], b: NoWrap[String]): NoWrap[String] = a + " " + b

    override def toString(v: NoWrap[Int]): NoWrap[String] = v.toString
  }

  type Nested[ScalaValue] = ScalaToLanguageBridge[ScalaValue]

  val simplify = new Language[Nested] {
    var nesting = 0

    override def number(v: Int): Nested[Int] = new ScalaToLanguageBridge[Int] {
      override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = {
        if(nesting > 0) {
          val temp = nesting
          nesting = 0
          L.add(L.number(temp), L.number(v))
        } else {
          L.number(v)
        }
      }
    }
    override def increment(a: ScalaToLanguageBridge[Int]): Nested[Int] = new ScalaToLanguageBridge[Int] {
      override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = {
        nesting = nesting + 1
        a.apply(L)
      }
    }
    override def add(a: ScalaToLanguageBridge[Int], b: ScalaToLanguageBridge[Int]): Nested[Int] = new ScalaToLanguageBridge[Int] {
      override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = {
        if(nesting > 0){
          val temp = nesting
          nesting = 0
          L.add(L.number(temp), L.add(a.apply(L), b.apply(L)))
        } else {
          L.add(a.apply(L), b.apply(L))
        }
      }
    }

    override def text(v: String): Nested[String] = ???

    override def toUpper(a: Nested[String]): Nested[String] = ???

    override def concat(a: Nested[String], b: Nested[String]): Nested[String] = ???

    override def toString(v: Nested[Int]): Nested[String] = ???
  }

}
