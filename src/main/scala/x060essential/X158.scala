package x060essential

import java.time.LocalDate

object X158 extends App {

  // the following type allows us to store a Visitor or any subtype because of [A >: Visitor]
  case class WebAnalytics[A >: Visitor](
    visitor: A,
    pageViews: Int,
    searchTerms: List[String],
    isOrganic: Boolean
  )

  val w1: WebAnalytics[Visitor] = WebAnalytics(Anonymous(1, LocalDate.now()), 2, List("qwe","rty"), isOrganic = false)
  val w2: WebAnalytics[Visitor] = WebAnalytics(User(13, "a@b.c", LocalDate.now()), 3, List("asd","qwe","rty"), isOrganic = true)
//  won't compile. ParentMain is super
//  val w3: WebAnalytics[Visitor] = WebAnalytics(ParentMain(33, "Dim"), 3, List("asd","qwe","rty"), true)

  // the following type allows us to store a Visitor or any super-type because of [A <: Visitor]
  case class WebAnalytics2[A <: Visitor](
    visitor: A,
    pageViews: Int,
    searchTerms: List[String],
    isOrganic: Boolean
  )

  val w4: WebAnalytics2[Visitor] = WebAnalytics2(Anonymous(1, LocalDate.now()), 2, List("qwe","rty"), isOrganic = false)
  val w5: WebAnalytics2[Visitor] = WebAnalytics2(User(13, "a@b.c", LocalDate.now()), 3, List("asd","qwe","rty"), isOrganic = true)
  // won't compile, ParentMain is not on the way to upper level
  // won't compile, I don't know why... 5.6.5, p. 158
//  val w6 = WebAnalytics2(new ParentInherit(), 3, List("asd","qwe","rty"), true)

  /**
    * 'Siamese' <: 'Cat' <: 'Animal'
    * 'Purr' <:' CatSound' <: 'Sound'
    */
  sealed trait Animal
  case class Cat(color: String, food: String) extends Animal
  sealed trait Siamese extends Cat

  sealed trait Sound
  sealed trait CatSound extends Sound
  sealed trait Purr extends CatSound

  /**
    * groomer if a function Cat => CatSound
    * the signature is contravariant to parameters: -Cat
    * the signature is covariant to return type: +CatSound
    * based the requirements, we can pass:
    *
    *  T     =>  R
    * Cat    => CatSound - invariance to T   , invariance to R
    * Cat    => Purr     - invariance to T   , covariance to R
    * Animal => CatSound - contravariant to T, invariance to R
    * Animal => Purr     - contravariant to T, covariance to R
    *
    * no way to use any other combinations.
    * compiler fill complain
    */
  val groomer1:   Cat     => CatSound = ???
  val groomer2:   Cat     => Purr     = ???
  val groomer3:   Animal  => CatSound = ???
  val groomer4:   Animal  => Purr     = ???
  val groomer5:   Siamese => Purr     = ???

  def groom(groomer: Cat => CatSound): CatSound = {
    val oswald = Cat("Black", "Cat food")
    groomer(oswald)
  }

  groom(groomer1)
  groom(groomer2)
  groom(groomer3)
  groom(groomer4)
  // will complain immediately!
//  groom(groomer5)

}
