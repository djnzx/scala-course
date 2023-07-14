package composition

object CatTh052 {
  
  type EA[A] = Either[Nothing, A] // ~ A
  /**
    * mult, add = rig = semi ring
    * mult, add, inv = ring
    * 
    * 2 = 1 + 1 ~ Boolean = true | false
    * 1 + a ~ Nothing | a
    *       ~ Either [(), a]
    * 
    */
  
}
