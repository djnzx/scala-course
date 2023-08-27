package javaish

import java.util.UUID
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

/** We have UUID in
  * - Java
  * - Postgres
  * - Linux/MacOS
  *   - uuid (ossp-uuid)
  *   - uuidgen
  * Questions:
  * - are they compatible
  * - how they are the same
  * - how they are different
  */
class ExploreUUID extends AnyFunSuite with Matchers with ScalaCheckPropertyChecks {

  test("uuid") {

    /** Version 1: Time-based UUID
      * Version 2: DCE Security UUID (rarely used)
      * Version 3: Name-based (MD5) UUID
      * Version 4: Randomly generated UUID
      * Version 5: Name-based (SHA-1) UUID
      */
    test("uuid") {
      val uuid = UUID.randomUUID
      System.out.println("Version: " + (uuid.version & 0xf))
      System.out.println("UUID: " + uuid)
    }

  }
}
