package aa_cookbook.x004

case class Address(city: String, state: String)

class PersonBase (var name: String, var address: Address) {
  override def toString: String = if (address == null) name else s"$name @ $address"
}

class Employee(name: String, address: Address, var age: Int)
// if parent class has more than one constructor
// here is the place where we control which one will be used in inheritance
  extends PersonBase(name, address)
