package meta

import org.scalatest.funspec.AnyFunSpec
import pprint._
import scala.meta._

import scala.meta.Source

class Playground extends AnyFunSpec {

  // https://astexplorer.net
  // https://scalameta.org/docs/trees/guide.html
  describe("tutorial") {

    it("1") {
      val program: String = """object Main extends App { print("Hello!" -> 1) }"""
      val tree: Source = program.parse[Source].get
      pprintln(tree)
    }

    it("2") {
      val path = java.nio.file.Paths.get("amt/src/main/scala/amt/parts", "AMT.scala")
      val bytes = java.nio.file.Files.readAllBytes(path)
      val text = new String(bytes, "UTF-8")
      val input = Input.VirtualFile(path.toString, text)
      val exampleTree1 = input.parse[Source].get // amt/src/main/scala/amt/parts/AMT.scala:35: error: } expected but end of file found
      val exampleTree2 = text.parse[Source].get  // <input>                               :35: error: } expected but end of file found
      pprintln(exampleTree1.syntax)
      pprintln(exampleTree2.syntax)
    }

    it("3") {
      pprintln("a + b".parse[Stat])
    }

    it("4") {
      pprintln(q"a.+(b)")
    }

    it("5") {
      pprintln("B with C".parse[Type])
    }

    it("6") {
      pprintln("class A extends B with C".parse[Stat])
    }


  }

}
