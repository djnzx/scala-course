package sbtx

class build_sbt {
  /**
    * https://blog.charleso.org/2018/11/scala-source-builds-new-hope.html
    * https://github.com/charleso/introduction-to-fp-in-scala
    *
    * lazy val root =
    *   (project in file("."))
    *     .dependsOn(RootProject(uri("ssh://git@github.com/foo/bar.git#78fb2722c598fc6d72ac47c069b6d004a34b6f5b")))
    * 
    * git submodule add ssh://git@github.com/foo/bar.git submodule/example
    * 
    * lazy val root =
    *   (project in file("."))
    *     .dependsOn(RootProject(file("submodule/example")))
    */
}
