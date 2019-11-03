package x00lessons.tips12

object Tip06FinalOrAbstract extends App {
  /**
    * MAKE METHODS FINAL OR ABSTRARCT
    *
    * not to brake something
    *
    */
  abstract class Creature {
    sealed def behavior1: Unit = println("behavior1")   // definitions
    abstract def behavior2: Unit                        // logic
  }
  final class SmartCreature extends Creature {
    override def behavior2: Unit = println("behavior2")
  }

}
