package winitzki

// https://blog.rockthejvm.com/scala-variance-positions/
// https://www.youtube.com/watch?v=aUmj7jnXet4

// https://blog.rockthejvm.com/contravariance/
// https://www.youtube.com/watch?v=b1ftkK1zhxI

class VariancePlayground extends Base {

  class Animal
  class Dog      extends Animal
  class Cat      extends Animal
  class Shepherd extends Dog
  class Spaniel  extends Dog

  test("MyList is invariant in type `A`") {

    case class MyList[A]()
    val xs: MyList[Dog] = MyList[Dog]()

    /** Won't compile, since `MyList is invariant in type A` */
//    val ys: MyList[Animal] = MyList[Dog]()
  }

  test("MyList is covariant in type `A`") {

    case class MyList[+A]()

    val xs: MyList[Dog] = MyList[Dog]()

    /** works fine, but erases subtype details */
    val ys: MyList[Animal] = MyList[Dog]()
  }

  test("Vet is contravariant in type `A`") {

    trait Vet[-A]

    /* Works well.
       The semantics is following:
          if I know how to heal Animal
          I will be able to heal Dog (possibly without details related to Dog)
     */
    val doctor: Vet[Dog] = new Vet[Animal] {}

    /* Won't compile
       The semantics is following:
          if I know how to heal Dog
          I have no any idea how to heal Cat and any other Animal
          since Dog is more specific than Animal
     */
//    val doctor2: Vet[Animal] = new Vet[Dog] {}

  }

  test("The problem #1") {
    /* Won't compile with the message
       Error: covariant type A occurs in contravariant position in type A of value elem
     */
    abstract class MyList[+A] {
//      def add(a: A): MyList[A]
    }
  }

  test("The Problem #2") {
    /* Won't compile with the message
       Error: contravariant type A occurs in covariant position in type A of value animal
     */
//    class Vet[-A](val animal: A)
//
//    val garfield = new Cat
//    val theVet: Vet[Animal] = new Vet[Animal](garfield)
//    val lassiesVet: Vet[Dog] = theVet

    /** First outcome (of problems 1 and 2):
      *
      * - if we return:  ... => A
      *   it's covariant position, so we can [widen] Dog to Animal
      *
      * - if we consume: A => ...
      *   it's contravariant position, we can't do that
      */
  }

  test("the problem #3") {
    sealed trait MOption[+A]

    case class SomeCo[+A](a: A) extends MOption[A]
    val x: SomeCo[Animal] = SomeCo[Dog](new Dog)

    /* Won't compile with the message:
       covariant type A occurs in contravariant position in type A of value a_
     */
//    case class SomeCo2[+A](var a: A) extends MOption[A]
    /** Outcome:
      *
      * - since var can be reassigned => we have more restrictions
      */
  }

  test("contra example #1") {
    class Vet[-A] {
      def heal(animal: A): Boolean = true
    }

    val vet: Vet[Animal] = new Vet[Animal]

    val dog = new Dog
    val cat = new Cat

    vet.heal(dog)
    vet.heal(cat)
  }

  test("contra example #2") {
    class Vet[-A] {
      def heal(animal: A): Boolean = true
    }

    val vet: Vet[Dog] = new Vet[Animal]

    val dog = new Dog
    val cat = new Cat

    vet.heal(dog)

    /** Won't compile */
//    vet.heal(cat)
  }

  test("return is covariant") {
    /* Won't compile with the message:
       contravariant type A occurs in covariant position in type (): A of method rescue
     */
//    abstract class Vet[-A] {
//      def rescue(): A
//    }

  }

  test("solution #1 - covariant type A occurs in contravariant position") {

    /** A is covariant, since we want to be able to add Cats and Dogs to Animals
      * +A means - we are okay with subtyping
      */
    class MyList[+A] {
      def head: A = ???
      def tail: MyList[A] = ???
      // covariant type A occurs in contravariant position in type A of value elem
      // def add(elem: A): MyList[A] = ???
      def add[A0 >: A](elem: A0): MyList[A0] = ???
    }

    /** A0       >: A
      *     means:
      * common   >: specific
      * -----------------------
      * Object   >: Animal
      * Animal   >: Dog
      * Dog      >: Shepherd
      */

    val l1: MyList[Shepherd] = new MyList[Shepherd]

    /** here, due to the annotation [A1 >: A]
      * A = Shepherd
      * A1 = Spaniel, but treated as a Dog to conform the restriction [A1 >: A]
      * since Dog >: Spaniel
      * compiler will find the most common type
      * and after operation MyList becomes of type Dog
      */
    val l2: MyList[Dog] = l1.add(new Spaniel)

    /** here, due to the annotation [A1 >: A]
      * A = Dog
      * A1 = Cat, but treated as an Animal to conform the restriction [A1 >: A]
      * since Animal >: Dog
      * compiler will find the most common type
      * and after operation MyList becomes of type Animal
      */
    val l3: MyList[Animal] = l2.add(new Cat)

    /** here, due to the annotation [A1 >: A]
      * A = Animal
      * A1 = String, but treated as an Object to conform the restriction [A1 >: A]
      * since Object >: Animal
      * compiler will find the most common type
      * and after operation MyList becomes of type Object
      */
    val l4: MyList[Object] = l3.add("hello")

    /** Scala standard library implemented exactly this way */
    val as: List[Shepherd] = new Shepherd :: Nil
    val bs: List[Dog] = new Spaniel :: as
    val cs: List[Animal] = new Cat :: bs
    val ds: List[Object] = "hello" :: cs

  }

  test("solution #2 - contravariant type A occurs in covariant position") {
    trait Vet[-A] {
      /* Won't compile with the message:
         contravariant type A occurs in covariant position in type (a: A): A of method heal
       */
      // def heal(a: A): A
      def heal[A1 <: A](a: A1): A1 = ???
    }

    val vet1: Vet[Shepherd] = new Vet[Shepherd] {}
    val x: Shepherd = vet1.heal(new Shepherd)
    /* Won't compile with the message:
       inferred type arguments [Spaniel] do not conform to method heal's
       type parameter bounds [A1 <: Shepherd]
     */
//    val y: Spaniel = vet1.heal(new Spaniel)
//    val y = vet1.heal(new Dog)

    /* Won't compile with the message:
       type mismatch;
         required: Vet[VariancePlayground.this.Dog]
         found   : Vet[VariancePlayground.this.Shepherd]
     */
//    val vet2: Vet[Dog] = new Vet[Shepherd]{}

    val vet2: Vet[Dog] = new Vet[Dog] {}
    vet2.heal(new Shepherd)
    vet2.heal(new Spaniel)
    /* Won't compile with the message:
       inferred type arguments [Cat] do not conform to method heal's type
       parameter bounds [A1 <: Dog]
     */
//    vet2.heal(new Cat)
    /* Won't compile with the message:
       inferred type arguments [Animal] do not conform to method heal's type
       parameter bounds [A1 <: Dog]
     */
//    vet2.heal(new Animal)
    /** A1       < : A
      *     means:
      * specific < : common (general)
      * -----------------------
      * Shepherd < : Dog
      * Dog      < : Animal
      * Animal   < : Object
      */

  }

  /** since Shepherd < (is a subtype, more specific)< Dog
    * then List[+A]:
    *   List[Shepherd] < (is a subtype, more specific)< List[Dog]
    * and
    * Vet[-A]
    *   Vet[Shepherd] can't heal Dog
    *   Vet[Dog] can heal Shepherd
    *   Vet[Dog] < Vet[Shepherd] makes sense
    *
    * - variance possibilities as answers to the variance question:
    *   - covariant (yes),
    *   - invariant (no),
    *   - contravariant (hell no, backwards)
    * - types val fields are in covariant position
    * - types of var fields are in covariant AND contravariant position
    * - types of method arguments are in contravariant position
    * - method return types are in covariant position
    * - we solve the “covariant type occurs in contravariant position”
    *     by "widening": we add a type argument [A0 >: A] and change the argument type to A0
    * - we solve the “contravariant type occurs in covariant position”
    *     by "narrowing": we add a type argument [A1 < : A] and change the method return type to A1
    *
    *  Scala Function1 trait is declared as
    *  Function1[-A, +B]
    */
}
