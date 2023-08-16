package tapp

import org.scalatest.Succeeded
import org.scalatest.funspec.AnyFunSpec

import java.nio.file.Paths

class AccessResources extends AnyFunSpec {
  describe("1"){
    it("3") {
      val url = getClass.getClassLoader.getResource("r")
      val uri = url.toURI
      val path = Paths.get(uri)
      println(path)
      Succeeded
    }
    it("4") {
      val url = getClass.getClassLoader.getResource("tpl")
      val uri = url.toURI
      val path = Paths.get(uri)
      println(path)
      Succeeded
    }
  }
}
