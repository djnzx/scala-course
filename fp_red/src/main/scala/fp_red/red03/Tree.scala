package fp_red.red03

import fp_red.red03.a.Tree

sealed trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {

  def size[A](t: Tree[A]): Int = ???

  def max(t: Tree[Int]): Int = ???
  
  def depth[A](t: Tree[A]): Int = ???
  
  def map[A,B](t: Tree[A])(f: A => B): Tree[B] = ???
  

  def fold[A,B](t: Tree[A])(f: A => B)(g: (B,B) => B): B = ???
  

  def sizeViaFold[A](t: Tree[A]): Int = ???

  def maxViaFold(t: Tree[Int]): Int = ???

  def depthViaFold[A](t: Tree[A]): Int = ???

  def mapViaFold[A,B](t: Tree[A])(f: A => B): Tree[B] = ???

}
