package hackerrank.d200320.ladybugs

object LadyBugsApp_Scala extends App {
  def hasSingles(bag: String) = bag.view
    .filter { _ != '_' }
    .groupBy { identity }
    .count { case (_, l) => l.size == 1 } > 0

  def squeeze(bag: String) = bag.indices
    .flatMap { idx =>
      val curr = bag(idx)
      val prev = if (idx == 0) '_' else bag(idx-1)
      if (curr != prev) Option(curr) else None
    }

  def alreadyOrdered(bag: String) = {
    val sq = squeeze(bag)
    sq.size == sq.distinct.length
  }

  def represent(value: Boolean) = if (value) "YES" else "NO"

  def happyLadybugs(bag: String) = represent(
    !hasSingles(bag) && (bag.contains("_") || alreadyOrdered(bag))
  )
}
