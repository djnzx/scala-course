package x004

case class Address(city: String, state: String)

class PersonBase (var name: String, var address: Address) {
  override def toString: String = if (address == null) name else s"$name @ $address"
}

class Employee(name: String, address: Address, var age: Int) extends PersonBase(name, address)
