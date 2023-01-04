package tools

import cats.implicits.toBifunctorOps
import cats.implicits.toFunctorOps
import collection.JavaConverters._
import java.io.BufferedWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream
import org.apache.avro.Schema
import org.apache.avro.file.DataFileReader
import org.apache.avro.file.DataFileWriter
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericRecord
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

  def writeTo(file: File, schema: Schema, items: GenericRecord*) = {
    val gw = new GenericDatumWriter[GenericRecord](schema)
    val w = new DataFileWriter[GenericRecord](gw)
    w.create(schema, file)
    items.foreach(w.append)
    w.close()
  }

  def read(file: File) = {
    val gr = new GenericDatumReader[GenericRecord]
    val r = new DataFileReader[GenericRecord](file, gr)
    val x = r.iterator().asScala.toList
    r.close()
    x
  }

  def read(file: File, schema: Schema) = {}

}
