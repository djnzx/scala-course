package degoes.tips12

object Tip06FinalOrAbstract extends App {
  /**
    * MAKE METHODS FINAL OR ABSTRACT
    *
    * not to brake something
    *
    */
  abstract class Creature {
    final def behavior1: Unit = println("behavior1")   // definitions
    def behavior2: Unit                               // logic
  }
  final class SmartCreature extends Creature {
    override def behavior2: Unit = println("behavior2")
  }

}
