package scalatest

import org.scalatest._
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

@WrapWith(classOf[ConfigMapWrapperSuite])
class Fix3Spec(configMap: Map[String, Any]) extends AnyFunSpec with Matchers {

  val host: String = configMap.get("host")
    .map(_.asInstanceOf[String])
    .getOrElse("fallback value")

  /**
    * http://aacctt.ddns.net:19901
    */
//  it("1") { config =>
//    val x: Option[String] = config.get("host").map(_.asInstanceOf[String])
//    pprint.log(x)
//  }
//  "Test" should "read variable" in { config =>
//    val x: Option[String] = config.get("abc").map(_.asInstanceOf[String])
//    pprint.log(x)
//  }

}
