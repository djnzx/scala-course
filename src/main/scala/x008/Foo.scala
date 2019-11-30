package x008

class Foo {
  def exec(f: String => Unit, name: String) = { f(name) }
}
