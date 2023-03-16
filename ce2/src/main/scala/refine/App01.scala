package refine

/**
  * https://kwark.github.io/refined-in-practice/#29
  * https://kwark.github.io/refined-in-practice/#53
  */
object App01 extends App {
  final case class Developer(name: String, twitterHandle: String)
  Developer("Peter", "@kwarkk") // ok
  Developer("@kwarkk", "Peter") // Oops, we've got a bug!

  final class Name(val value: String) extends AnyVal
  final class TwitterHandle (val value: String) extends AnyVal
  final case class Developer2(name: Name, twitterHandle: TwitterHandle)
  Developer2(new Name("@kwarkk"), new TwitterHandle("Peter"))
//  Developer(new TwitterHandle("@kwarkk"), new Name("Peter"))
  Developer2(new Name("Peter"), new TwitterHandle("@kwarkk"))
  /** refinement = base type + ∀ satisfy predicate */

  import eu.timepit.refined.api._;
  import eu.timepit.refined.collection._;
  import eu.timepit.refined.string._;
  import eu.timepit.refined._;
  import eu.timepit.refined.auto._
  import eu.timepit.refined.api.Refined
  import eu.timepit.refined.collection.NonEmpty
  import eu.timepit.refined.string.StartsWith
  import eu.timepit.refined.W

  /**
    * https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0#singleton-typed-literals
    */
  object refinements {
    type Name = Refined[String, NonEmpty]
    type TwitterHandle = String Refined StartsWith[W.`"@"`.T]
    final case class Developer(name: Name, twitterHandle: TwitterHandle)
  }
  
  "foo"
  /** 
    * is a:
    * - String
    * - Any
    * - AnyRef
    * - "foo"
    * */
  // compile time checking
  val n = "Peter Mortier": refinements.Name
  // will fail at compile time
//  "foo": refinements.TwitterHandle
  refinements.Developer("Peter Mortier", "@kwarkk")
  // doesn't
//  refinements.Developer("@kwarkk", "Peter Mortier")
  import eu.timepit.refined.api.RefType
  
  val n1: refinements.Name = RefType.applyRefM[refinements.Name]("Peter Mortier")
  // doesn't
//  RefType.applyRefM[refinements.TwitterHandle]("wrong")
 
  val name: String = "Peter Mortier"
  // or left
//  val n1: Either[String, refinements.Name] = RefType.applyRef[refinements.Name](name)
  /**
    * compile time:	eu.timepit.refined.api.RefType.applyRefM[RT]: RT
    * runtime:    	eu.timepit.refined.api.RefType.applyRef[RT]: Either[String, RT]
    * 
    * char prdeicates
    * 
    */
}
