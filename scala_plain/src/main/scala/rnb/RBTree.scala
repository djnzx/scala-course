package rnb

class RBTree[K: Ordering, V] {
  
//  trait Color
//  final case object Red extends Color
//  final case object Black extends Color
  
  val RED = true
  val BLACK = false
  
  case class Node(key: K, value: V, color: Boolean, left: Node, right: Node) {
    def this(k: K, v: V, c: Boolean) = this(k, v, c, null, null)
    def withV(v: V) = copy(value = v)
    def withL(l: Node) = copy(left = l)
    def withR(r: Node) = copy(right = r)
    def withLR(l: Node, r: Node) = copy(left = l, right = r)
    def withLRC(l: Node, r: Node, c: Boolean) = copy(left = l, right = r, color = c)
    def withColor(c: Boolean) = copy(color = c)
  }

  /** nulls are BLACK */
  def isRed(x: Node) =
    x != null && x.color == RED
  
  def rotateRight(d: Node) = {
    require(d != null, "rotateRight: Node to rotate mustn't be null!")
    require(isRed(d.left), "rotateRight: Left node must be RED!")
    
    val b = d.left
    val a = b.left
    val c = b.right
    val e = d.right
    
    val newD = d.withLRC(c, e, RED) /** rotated node becomes RED */
    b.withLRC(a, newD, d.color)
  }
  
  def rotateLeft(b: Node) = {
    require(b != null, "rotateLeft: Node to rotate mustn't be null!")
    require(isRed(b.right), "rotateLeft: Right node must be RED!")
    
    val a = b.left
    val d = b.right
    val c = d.left
    val e = d.right
    
    val newB = b.withLR(a, c).withColor(RED) /** rotated node becomes RED */
    d.withLRC(newB, e, b.color)
  }
  
  def flipColors(x: Node) = {
    require(!isRed(x), "flipColors: incoming must be BLACK")
    require(isRed(x.left), "flipColors: left must be RED")
    require(isRed(x.right), "flipColors: right must be RED")
    
    val newL = x.left.withColor(BLACK)
    val newR = x.right.withColor(BLACK)
    x.withLRC(newL, newR, RED)
  }

  var root: Node = _
  
  def add(h: Node, k: K, v: V)(implicit ok: Ordering[K]): Node = {
    if (h == null) return new Node(k, v, RED) /** new nodes are always RED */
    val cmp = ok.compare(k, h.key)
    
    val h2 =
      if      (cmp < 0) { h.withL(add(h.left,  k, v)(ok)) } // new k is less than current
      else if (cmp > 0) { h.withR(add(h.right, k, v)(ok)) } // new k is greater than current
      else              { h.withV(v)                      } // new k is the same, replace the val
    
    /** balance */
    val h3 = if ( isRed(h2.right) && !isRed(h2.left)      ) rotateLeft(h2)  else h2
    val h4 = if ( isRed(h3.left)  &&  isRed(h3.left.left) ) rotateRight(h3) else h3
    val h5 = if ( isRed(h4.left)  &&  isRed(h4.right)     ) flipColors(h4)   else h4
    
    h5
  }
  
  def add(k: K, v: V): Unit = {
    root = add(root, k, v)
      .withColor(BLACK)
  }
  
}
