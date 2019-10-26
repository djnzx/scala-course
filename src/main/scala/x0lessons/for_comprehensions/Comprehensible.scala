package x0lessons.for_comprehensions

abstract class Comprehensible[A] {
  def map[B](f: A => B): Comprehensible[B]
  def flatMap[B](f: A => Comprehensible[B]): Comprehensible[B]
  def withFilter(p: A => Boolean): Comprehensible[A]
  def foreach(f: A => Unit): Unit
}
