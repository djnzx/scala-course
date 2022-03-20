package protox

import pbx.student.Student

import scala.util.chaining.scalaUtilChainingOps

/** class [[Student]] is automatically generated in the `target/scala-2.13/src_managed/main/scalapb/pbx/student` from
  * `src/main/protobuf/student.proto` file by running `sbt scala_plain/protocGenerate` <p> to configure:
  *   - project/protobuf.sbt
  *   - build.sbt: Compile / PB.targets := Seq( scalapb.gen() -> (Compile / sourceManaged).value / "scalapb" )
  */
object ProtobufSimpleApp extends App {

  /** create */
  val student: Student = new Student(1, "Jim")

  /** { "name" : "Jim", "id" : 2 } */

  /** serialize object to bytes */
  val bytes: Array[Byte] = student.toByteArray

  /** deserialize bytes to object */
  val student2: Student = Student.parseFrom(bytes)

  /** check equality */
  println(student == student2)

  /** print bytes */
  pprint.pprintln(bytes)

  /** print bytes in HEX format */
  bytes.map(x => f"$x%02X").mkString(", ").pipe(println)
}
