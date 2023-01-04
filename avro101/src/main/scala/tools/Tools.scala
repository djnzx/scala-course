package tools

import cats.implicits.catsSyntaxOptionId
import cats.implicits.toBifunctorOps
import cats.implicits.toFunctorOps
import java.io.ByteArrayOutputStream
import java.io.File
import org.apache.avro.Schema
import org.apache.avro.file.DataFileReader
import org.apache.avro.file.DataFileWriter
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericRecord
import scala.collection.JavaConverters._
import scala.util.Try

object Tools {

  def validateRecord(record: GenericRecord) = {
    val schema = record.getSchema
    val gw = new GenericDatumWriter[GenericRecord](schema)
    val w = new DataFileWriter[GenericRecord](gw)
    val os = new ByteArrayOutputStream()
    w.create(schema, os)
    val x = Try(w.append(record)).toEither.leftMap(_.getCause.getMessage).as(record)
    w.close()
    x
  }

  /** write to file with schema */
  def writeTo(file: File, schema: Schema, items: GenericRecord*) = {
    val gw = new GenericDatumWriter[GenericRecord](schema)
    val w = new DataFileWriter[GenericRecord](gw)
    w.create(schema, file)
    items.foreach(w.append)
    w.close()
  }

  private def readFrom(file: File, maybeSchema: Option[Schema]): List[GenericRecord] = {
    val gr = maybeSchema.fold(
      new GenericDatumReader[GenericRecord]
    ) { schema =>
      new GenericDatumReader[GenericRecord](schema)
    }
    val r = new DataFileReader[GenericRecord](file, gr)
    val x = r.iterator().asScala.toList
    r.close()
    x
  }

  /** read without schema check enforced */
  def readFrom(file: File): List[GenericRecord] =
    readFrom(file, None)

  /** read WITH schema check enforced */
  def readFrom(file: File, schema: Schema): List[GenericRecord] =
    readFrom(file, schema.some)

}
