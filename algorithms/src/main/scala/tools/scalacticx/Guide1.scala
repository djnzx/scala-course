package tools.scalacticx

import org.scalactic._

object Guide1 {

  /**
    * 1. triple equals.
    * Some(1) === 1
    * will brake IN COMPILE TIME on unrelated types
    * 1 === 1L
    * will brake IN COMPILE TIME on unrelated types (Byte, Short, Int, Long)
    */
  import TypeCheckedTripleEquals._
//  Some(1) === 1
//  1 === 1L

  /**
    * will make them true...
    */
  import TraversableEqualityConstraints._
  List(1,2,3) === Vector(1,2,3)
  
}
