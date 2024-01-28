package monadismonoid

object CategoryDefinition {

  trait Composable[F[_, _]] {
    def compose[A, B, C](f: F[B, C], g: F[A, B]): F[A, C]
    def andThen[A, B, C](f: F[A, B], g: F[B, C]): F[A, C] = compose(g, f)
  }

  trait Category[F[_, _]] extends Composable[F] { self =>
    def id[A]: F[A, A]
  }


//
//
//  // Define a trait for objects in the category
//  trait ObjectCategory[A]
//
//  // Define a trait for morphisms (arrows) in the category
//  trait MorphismCategory[A, B] {
//    def compose[C](g: MorphismCategory[B, C]): MorphismCategory[A, C]
//  }
//
//  // Define a higher-order type for the category itself
//  trait Category[O[_], M[_, _]] {
//    def identity[A]: M[A, A]
//  }
//
//  // Example: Define a concrete category for the List type
//  object ListCategory extends Category[List, Function1] {
//    def identity[A]: Function1[A, A] = (a: A) => a
//  }
//
//  // Example: Define a concrete object in the List category
//  implicit def listObject[A]: ObjectCategory[List[A]] = new ObjectCategory[List[A]] {}
//
//  // Example: Define a concrete morphism in the List category
////  implicit def listMorphism[A, B]: MorphismCategory[List[A], List[B]] = new MorphismCategory[List[A], List[B]] {
////
////    override def compose[C](g: MorphismCategory[List[B], C]): MorphismCategory[List[A], C] = new MorphismCategory[List[A], C] {
////      override def compose[D](g: MorphismCategory[C, D]): MorphismCategory[List[A], D] = listMorphism[A, D]
////    }
////  }

}
