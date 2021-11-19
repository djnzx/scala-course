package tinkoff

import scala.annotation.tailrec

/** 1) Cжать последовательность интов Seq(1, 2, 2, 3, 4, 3, 3, 3) => Seq((1, 1), (2, 2), (3, 1), (4, 1), (3, 3))
  *
  * Ответ выдать в виде Seq[(Int, Int)] (число из последовательности и число последовательных повторений) 2)
  * восстановаить исходную последовательность из сжатой
  */
object Tinkoff1 extends App {

  def squeeze(xs: Seq[Int]): Seq[(Int, Int)] = {

    @tailrec
    def fold(xs: Seq[Int], curItem: Option[Int], cnt: Int, buffer: Seq[(Int, Int)]): Seq[(Int, Int)] = {
      xs match {
        case Nil if cnt > 0 => ((curItem.get, cnt) +: buffer).reverse
        case Nil => buffer.reverse
        case h :: t if curItem == Some(h) => fold(t, curItem, cnt + 1, buffer)
        case h :: t if curItem == None => fold(t, Some(h), 1, buffer)
        case h :: t => fold(t, Some(h), 1, (curItem.get, cnt) +: buffer)
      }
    }

    fold(xs, None, 0, Seq.empty)
  }

//  val r = squeeze(Seq(1, 2, 2, 3, 4, 3, 3, 3))
//  pprint.pprintln(r)

  def unzip(xs: Seq[(Int, Int)]): Seq[Int] =
    xs.flatMap { case (n, c) => Seq.fill(c)(n) }

//  val r: Seq[Int] = unzip(List((1, 1), (2, 2), (3, 1), (4, 1), (3, 3)))
//  pprint.pprintln(r)

}
