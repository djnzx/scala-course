package c07x01_LargestFiles
import java.io.InputStream

import geny.Generator
import os.{Path, RelPath}

object LargestFilesApp extends App {
  /**
    * os.pwd = current project root
    */
  val r: 
    Seq[Path] = 
//    Seq[(Long, Path)] = 
    os.walk(os.pwd)
      .filter(os.isFile)
      .map(path => (os.size(path), path))
      .sortBy(-_._1)
      .map { case (_, path) => path }
      .take(5)
//  pprint.pprintln(r)
  pprint.pprintln(os.pwd.segments.toList)

  /**
    * os.Path("/home")   - absolute
    * os.RelPath("home") - relative
    */

  val p1: Path = os.root
  val p2: Path = os.pwd
  val p3: Path = os.home
  val p4: RelPath = os.up
  /**
    * list from the current dir
     */
  pprint.log(
    os.list(os.pwd)
      .filter(os.isFile)
  )
  // produce iterator
  val dir1: Seq[Path] = os.walk(os.pwd)
  // produce generator
  val dir2: Generator[Path] = os.walk.stream(os.pwd)
  
  pprint.log(os.stat(os.pwd))

  val path1 = os.pwd / "new.txt"
  os.write(path1, "Hello")
  val contents = os.read(path1)
  val bytes: Array[Byte] = os.read.bytes(path1)
  val is: InputStream = os.read.inputStream(path1)
  //os.makeDir
  //os.remove
  pprint.log(contents)
}
