import os.Path

package object c09 {
  val current: Path = os.pwd / "lihaoyi" / "src" / "main" / "scala" / "c09"
  val posts: Path = current / "post"
  val out: Path = current / "out"
}
