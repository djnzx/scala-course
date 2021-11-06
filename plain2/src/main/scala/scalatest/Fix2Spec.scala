package scalatest

import java.io.{File, FileWriter}

import org.scalatest._
import org.scalatest.flatspec.FixtureAnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Fix2Spec extends FixtureAnyFlatSpec with fixture.ConfigMapFixture with Matchers {

//  case class FixtureParam(file: File, writer: FileWriter)

//  override def withFixture(test: OneArgTest) = {
//    val file = File.createTempFile("hello", "world") // create the fixture
//    val writer = new FileWriter(file)
//    val theFixture = FixtureParam(file, writer)
//
//    try {
//      writer.write("ScalaTest is ") // set up the fixture
//      withFixture(test.toNoArgTest(theFixture)) // "loan" the fixture to the test
//    }
//    finally writer.close() // clean up the fixture
//  }
  "Test" should "read variable" in { config =>
    val x: Option[Any] = config.get("abc")
    pprint.log(x)
  }


  "The config map" should "contain hello" in { configMap: FixtureParam =>
    configMap should contain key "hello"
  }

  it should "contain world" in { configMap =>
    configMap should contain key "world"
  }

//  "Testing" should "be easy" in { f =>
//    f.writer.write("easy!")
//    f.writer.flush()
//    assert(f.file.length === 18)
//  }


}
