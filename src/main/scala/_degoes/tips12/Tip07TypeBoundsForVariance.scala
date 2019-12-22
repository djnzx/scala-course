package _degoes.tips12

import shapeless.Prism

class Tip07TypeBoundsForVariance {
  // first intention
  trait ProductID
  trait MovieID extends ProductID
  // DO NOT use type bounds, dont constraint !
  trait Product[T <: ProductID] {
    def id: T
  }

  trait Movie extends Product[MovieID]

  // better. DO NOT limit yourself and remove entangling
  trait ProductGood[T] {
    def id: T
    // runtime proof that every T is a ProductID
    def prism: Prism[ProductID, T]
  }
  trait MovieGood extends ProductGood[MovieID]

}
