package _ait

import munit.FunSuite
import org.scalatest.matchers.should.Matchers

class IslandRelatedTasksSpec extends FunSuite with Matchers {

  val surface: Array[Array[Int]] = Array(
    Array(0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0),
    Array(0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 0),
    Array(0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0),
    Array(0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0),
    Array(0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0)
  )

  val app = new IslandRelatedTasks(surface)

  test("maxIslandArea") {
    app.maxIslandArea shouldEqual 14
  }

  test("maxIslandArea points") {
    app.firstMaxIslandSize shouldEqual Set(
      Pt(12, 2),
      Pt(10, 3),
      Pt(9, 2),
      Pt(9, 0),
      Pt(9, 1),
      Pt(11, 4),
      Pt(11, 1),
      Pt(10, 2),
      Pt(12, 3),
      Pt(10, 4),
      Pt(12, 4),
      Pt(10, 0),
      Pt(11, 0),
      Pt(12, 1)
    )
  }

  test("islands count") {
    app.islandCount shouldEqual 5
  }

}
