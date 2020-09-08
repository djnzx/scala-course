package mortals

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Mortals003aSpec extends AnyFlatSpec with Matchers {
  import Mortals003a.Mutable
  import Mortals003a.Data._
  import Mortals003a.WorldView

  "Business Logic" should "generate an initial world view" in {
    val mutable = new Mutable(needsAgents)
    import mutable._
    
    program.initial shouldBe needsAgents
  }

  it should "remove changed nodes from pending" in {
    val world = WorldView(0, 0, managed, Map(node1 -> time3), Map.empty, time3)
    val mutable = new Mutable(world)
    import mutable._
    
    val old = world.copy(
      alive = Map.empty, 
      pending = Map(node1 -> time2), 
      time = time2)
    
    program.update(old) shouldBe world
  }

  it should "request agents when needed" in { 
    val mutable = new Mutable(needsAgents)
    import mutable._
    val expected = needsAgents.copy(
      pending = Map(node1 -> time1))
    
    program.act(needsAgents) shouldBe expected
    mutable.stopped shouldBe 0
    mutable.started shouldBe 1
  }
  
  /**
    * TODO: tests to implement:
    * - not request agents when pending
    * - don’t shut down agents if nodes are too young
    * - shut down agents when there is no backlog and nodes will shortly incur new costs 
    * - not shut down agents if there are pending actions
    * - shut down agents when there is no backlog if they are too old
    * - shut down agents, even if they are potentially doing work, if they are too old
    * - ignore unresponsive pending actions during update
    * TODO: how to run TestSpecs programmatically?
    */
}
