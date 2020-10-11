package tools

import scala.reflect.ClassTag

object Data {
  def a[A: ClassTag](aa: A*): Array[A] = aa.toArray
}
